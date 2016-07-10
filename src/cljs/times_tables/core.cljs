(ns times-tables.core
  (:require [reagent.core :as r]
            [times-tables.app :as a]
            [times-tables.inputs :as i]
            [times-tables.util :as u]
            [times-tables.canvas :as c]))

(defn controls [{:keys [dots times]}]
  [:div.controls
   [:div.col.labels
    [:span "Dots:"]
    [:span "Times:"]]
   [:div.col.text
    [i/input-text {:value (:value dots)
                   :set-value (:set-value dots)
                   :increment a/increment-dots}]
    [i/input-text {:value (:value times)
                   :set-value (:set-value times)
                   :increment a/increment-times}]]
   [:div.col.range
    [i/input-range {:value (:value dots)
                    :set-value (:set-value dots)
                    :min-valye a/min-dots
                    :max-value a/max-dots
                    :step a/increment-dots}]
    [i/input-range {:value (:value times)
                    :set-value (:set-value times)
                    :min-value a/min-times
                    :max-value a/max-times
                    :step a/increment-times}]]])

(defn set-dots [state v]
  (when-let [v (a/->dots v)]
    (swap! state assoc :dots v)
    v))

(defn set-times [state v]
  (when-let [v (a/->times v)]
    (swap! state assoc :times v)
    v))

(defn page [state]
  (let [{:keys [dots times]} @state
        ctx {:dots {:value dots
                    :set-value (partial set-dots state)}
             :times {:value times
                     :set-value (partial set-times state)}}]
    [:div.page
     [controls ctx]
     [c/times-table ctx]]))

(defonce app-state (r/atom {:dots 10
                            :times 2}))

(defn reload []
  (r/render [page app-state] (.getElementById js/document "app")))

(defn ^:export main []
  (u/dev-setup)
  (reload))
