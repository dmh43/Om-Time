(ns om-time.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-time.timer :as t]))

(defn new-time-event-handler
  [owner set-time-chan]
  (go (loop []
        (let [new-time (<! set-time-chan)]
          (t/start-stop-timer owner)
          (om/set-state! owner :sec-remaining new-time)
          (t/start-stop-timer owner)
          (recur)))))

(defn start-stop-event-handler
  [owner start-stop-chan]
  (go (loop []
        (let [pp-event (<! start-stop-chan)]
          (t/start-stop-timer owner)
          (recur)))))
