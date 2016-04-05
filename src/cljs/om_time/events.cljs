(ns om-time.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-time.timer :as t]))

(defn new-time-event-handler
  [cursor]
  (let [set-time (get-in cursor [:events :set-time])]
    (go (loop []
          (let [new-time (<! set-time)]
            (t/stop-timer cursor)
            (om/transact! cursor :sec-remaining #(identity new-time))
            (recur))))))

(defn start-stop-event-handler
  [cursor]
  (let [play-pause (get-in cursor [:events :play-pause])]
    (go (loop []
          (let [pp-event (<! play-pause)]
            (t/start-stop-timer cursor)
            (recur))))))
