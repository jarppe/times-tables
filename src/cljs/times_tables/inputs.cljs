(ns times-tables.inputs
  (:require [reagent.core :as r]
            [times-tables.util :as u]))

(defn input-text [{:keys [value set-value increment]}]
  (let [local (r/atom {:value value
                       :editing? false})
        on-change (fn [e] (swap! local assoc :value (-> e .-target .-value)))
        on-focus (fn [_] (swap! local assoc :editing? true))
        on-blur (fn [_]
                  (-> local
                      (swap! assoc :editing? false)
                      :value
                      (set-value)))
        set-new-value (fn [e new-value]
                        (.preventDefault e)
                        (some->> new-value
                                 (set-value)
                                 (swap! local assoc :value)))
        on-key-down (fn [e]
                      (let [key-code (-> e .-keyCode)
                            new-value (-> e .-target .-value u/->num)]
                        (if new-value
                          (condp = key-code
                            13 (set-new-value e new-value)
                            38 (set-new-value e (+ new-value increment))
                            40 (set-new-value e (- new-value increment))
                            nil))))]
    (fn [{:keys [value]}]
      (let [value (if (:editing? @local)
                    (:value @local)
                    (do
                      (swap! local assoc :value value)
                      value))]
        [:input.text {:type 'text
                      :value value
                      :on-change on-change
                      :on-key-down on-key-down
                      :on-focus on-focus
                      :on-blur on-blur}]))))

(defn input-range [{:keys [value set-value min-value max-value step]}]
  [:input.range {:type 'range
                 :min min-value
                 :max max-value
                 :step step
                 :value value
                 :on-change (fn [e] (->> e .-target .-value set-value))}])

