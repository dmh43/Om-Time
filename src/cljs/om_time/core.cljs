(ns om-time.core
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [plumbing.core :as p])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-time.clock :as c]
            [om-time.events :as e]
            [devtools.core :as devtools]
            [om-time.personal :as p]))

(devtools/install!)

(enable-console-print!)

(def standard-time (* 60 20))

(defonce app-state (atom {:events {:set-time (chan)
                                   :play-pause (chan)}
                          :sec-remaining standard-time
                          :timer nil
                          :base-time standard-time
                          :counting? false}))

(def root-cursor (om/root-cursor app-state))
(def ref (om/ref-cursor root-cursor))

(defn root-component [app owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (e/new-time-event-handler ref)
      (e/start-stop-event-handler ref))

    om/IDidMount
    (did-mount [_]
      (om/transact! ref :sec-remaining #(get ref :base-time)))

    om/IRender
    (render [_]
      (dom/div
       #js {:className "root"}
       (om/build c/clock ref)
       #_(om/build p/personal nil)))))

(om/root
 root-component
 app-state
 {:target (js/document.getElementById "app")})
