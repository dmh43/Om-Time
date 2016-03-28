(ns om-time.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [om-time.core-test]))

(enable-console-print!)

(doo-tests 'om-time.core-test)
