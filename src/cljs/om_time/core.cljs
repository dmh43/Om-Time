(ns om-time.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]
            [cljs.core.async :refer [put! chan <!]]
            [om-time.clock :as c]))

(enable-console-print!)

(defonce app-state (atom {:text "Relax" :sec-remaining 0}))

(defn stop-timer
  [timer owner]
  (js/clearInterval timer)
  (om/set-state! owner :timer nil))

(defn start-timer
  [owner]
  (let [timer (js/setInterval
               #(om/update-state! owner :sec-remaining dec)
               1000)]
    (om/set-state! owner :timer timer)))

(defn start-stop-timer
  [owner]
  (let [timer (om/get-state owner :timer)]
    (if timer
      (stop-timer timer owner)
      (start-timer owner))))

(defn root-component [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:events {:set-time (chan)
                :play-pause (chan)}
       :sec-remaining 1000
       :timer nil})

    om/IWillMount
    (will-mount [_]
      (let [set-time (om/get-state owner [:events :set-time])
            play-pause (om/get-state owner [:events :play-pause])]
        (go (loop []
              (let [new-time (<! set-time)]
                (om/set-state! owner :sec-remaining new-time)
                (recur))))
        (go (loop []
              (let [pp-event (<! play-pause)]
                (start-stop-timer owner)
                (recur))))))

    om/IRenderState
    (render-state [_ {{:keys [play-pause set-time]} :events}]
      (dom/div
       {:className "root"}
       (om/build c/clock (om/get-state owner :sec-remaining) {:init-state {:play-pause play-pause :set-time set-time}})))))

(om/root
 root-component
 app-state
 {:target (js/document.getElementById "app")})
