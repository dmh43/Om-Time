(ns om-time.timer
  (:require [om.core :as om :include-macros true]))

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
