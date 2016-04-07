(ns om-time.personal
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn personal [_ owner]
  (reify
    om/IRender
    (render [_]
      (dom/div
       #js {:className "personal"}
       (dom/a
        #js {:href "https://www.github.com/dmh43"}
        (dom/i #js {:className "fa fa-github"}))
       (dom/a
        #js {:href "https://github.com/dmh43/Om-Time"}
        (dom/i #js {:className "fa fa-code-fork"}))
       (dom/a
        #js {:href "https://twitter.com/danyhaddad43"}
        (dom/i #js {:className "fa fa-twitter"}))))))
