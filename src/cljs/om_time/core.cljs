(ns om-time.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-time.clock :as c]
            [om-time.timer :as t]
            [om-time.events :as e]))

(enable-console-print!)

(defonce app-state (atom {:text "Relax"}))

(def standard-time (* 20 60))

(defn root-component [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:events {:set-time (chan)
                :play-pause (chan)}
       :sec-remaining 0
       :timer nil
       :base-time standard-time})

    om/IWillMount
    (will-mount [_]
      (let [set-time-chan (om/get-state owner [:events :set-time])
            start-stop-chan (om/get-state owner [:events :play-pause])]
        (e/new-time-event-handler owner set-time-chan)
        (e/start-stop-event-handler owner start-stop-chan)))

    om/IDidMount
    (did-mount [_]
      (let [base-time (om/get-state owner :base-time)]
        (om/set-state! owner :sec-remaining base-time)))

    om/IRenderState
    (render-state [_ {{:keys [play-pause set-time]} :events
                      :keys [base-time]}]
      (dom/div
       {:className "root"}
       (dom/h1
        #js {:className "title"}
        (:text @app-state))
       (om/build c/clock
                 (om/get-state owner :sec-remaining)
                 {:init-state {:play-pause-events play-pause
                               :set-time-events set-time
                               :base-time base-time}})))))

(om/root
 root-component
 app-state
 {:target (js/document.getElementById "app")})
