(ns times-tables.inputs
  (:require [reagent.core :as r]))

(defn input-text [value set-new-value]
  (let [local (r/atom {:value value
                       :editing? false})
        on-change (fn [e]
                    (swap! local assoc :value (-> e .-target .-value)))
        on-focus (fn [_]
                   (swap! local assoc :editing? true))
        on-blur (fn [_]
                  (-> local
                      (swap! assoc :editing? false)
                      :value
                      (set-new-value)))
        on-key-down (fn [e]
                      (if (-> e .-keyCode (= 13))
                        (set-new-value (-> e .-target .-value))))]
    (fn [new-value _]
      (let [value (if (:editing? @local)
                    (:value @local)
                    (do
                      (swap! local assoc :value new-value)
                      new-value))]
        [:input.text {:type 'text
                      :value value
                      :on-change on-change
                      :on-key-down on-key-down
                      :on-focus on-focus
                      :on-blur on-blur}]))))

(defn input-range [min-value max-value step value set-new-value]
  [:input.range {:type 'range
                 :min min-value
                 :max max-value
                 :step step
                 :value value
                 :on-change (fn [e] (->> e .-target .-value set-new-value))}])

