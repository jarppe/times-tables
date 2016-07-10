(ns times-tables.app
  (:require [times-tables.util :as u]))

(def min-dots 2)
(def max-dots 200)
(def increment-dots 1)
(def min-times 2)
(def max-times 102)
(def increment-times 0.1)

(defn ->dots [v]
  (some->> v (u/->num) (u/round 0) (u/bound min-dots max-dots)))

(defn ->times [v]
  (some->> v (u/->num) (u/round 1) (u/bound min-times max-times)))
