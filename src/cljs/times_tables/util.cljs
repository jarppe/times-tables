(ns times-tables.util)

(defonce debug? ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn rgb [r g b]
  (str "rgb(" r "," g "," b ")"))

(defn rgba [r g b a]
  (str "rgba(" r "," g "," b "," a ")"))

(defn ->num [v]
  (if (number? v)
    v
    (let [n (js/parseFloat v)]
      (if (not (js/isNaN n))
        n))))

(defn ->bound [min-v max-v v]
  (-> v (max min-v) (min max-v)))

