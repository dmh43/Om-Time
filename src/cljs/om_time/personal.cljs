(ns om-time.personal
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]))

(defn personal [_ owner]
  (reify
    om/IRender
    (render [_]
      (dom/div
       #js {:className "personal"}
       (dom/div
        #js {:href "www.github.com/dmh43"}
        (r/glyphicon {:glyph "user"}))))))
