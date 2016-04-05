(ns om-time.timer
  (:require [om.core :as om :include-macros true]))

(defn buzzer
  []
  (.play (js/Audio. "./Tone.mp3")))

(defn stop-timer
  ([cursor]
   (stop-timer (get cursor :timer) cursor))
  ([timer cursor]
   (js/clearInterval timer)
   (om/transact! cursor :timer #(identity nil))))

(defn on-times-up
  [cursor]
  (buzzer)
  (stop-timer cursor))

(defn time-keeper
  [cursor]
  (om/transact! cursor :sec-remaining dec)
  (when (= 0 (get cursor :sec-remaining))
    (on-times-up cursor))
  (js/console.log (om/value @cursor)))

(defn start-timer
  [cursor]
  (let [timer (js/setInterval #(time-keeper cursor) 1000)]
    (om/transact! cursor :timer #(identity timer))))

(defn start-stop-timer
  [cursor]
  (let [timer (get cursor :timer)]
    (if timer
      (stop-timer timer cursor)
      (start-timer cursor))))
