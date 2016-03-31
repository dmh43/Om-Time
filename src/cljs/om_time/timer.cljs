(ns om-time.timer
  (:require [om.core :as om :include-macros true]))

(defn buzzer
  []
  (.play (js/Audio. "./Tone.mp3")))

(defn stop-timer
  ([owner]
   (stop-timer (om/get-state owner :timer) owner))
  ([timer owner]
   (js/clearInterval timer)
   (om/set-state! owner :timer nil)))

(defn on-times-up
  [owner]
  (buzzer)
  (stop-timer owner))

(defn time-keeper
  [owner]
  (om/update-state! owner :sec-remaining dec)
  (when (= 0 (om/get-state owner :sec-remaining))
    (on-times-up owner)))

(defn start-timer
  [owner]
  (let [timer (js/setInterval #(time-keeper owner) 1000)]
    (om/set-state! owner :timer timer)))

(defn start-stop-timer
  [owner]
  (let [timer (om/get-state owner :timer)]
    (if timer
      (stop-timer timer owner)
      (start-timer owner))))
