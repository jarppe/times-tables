(ns times-tables.core
  (:require [reagent.core :as r]
            [times-tables.inputs :as i]
            [times-tables.util :as u]))

(def min-dots 2)
(def max-dots 200)
(def min-times 2)
(def max-times 102)
(def PIx2 (* Math/PI 2.0))
(def background (u/rgb 58 58 58))

(defonce app-state (r/atom {:dots 10
                            :times 2}))

(defn a->xy [r d]
  [(* r (Math/cos d))
   (* r (Math/sin d))])

(defn start-drawing [{:keys [canvas dctx width height]}]
  (doto canvas
    (aset "width" width)
    (aset "height" height))
  (doto dctx
    (aset "fillStyle" background)
    (.fillRect 0 0 width height))
  (.save dctx)
  (.translate dctx (* width 0.5) (* height 0.5))
  (.rotate dctx Math/PI))

(defn end-drawing [{:keys [dctx]}]
  (.restore dctx))

(defn draw-border [{:keys [dctx r]}]
  (doto dctx
    (aset "strokeStyle" (u/rgba 128 128 128 0.5))
    (.beginPath)
    (.arc 0 0 r 0 PIx2 false)
    (.stroke)))

(defn draw-dot [dctx [x y]]
  (doto dctx
    (.beginPath)
    (.arc x y 5 0 PIx2 false)
    (.fill)))

(defn draw-dots [{:keys [dctx da r]}]
  (aset dctx "fillStyle" (u/rgb 192 32 32))
  (->> (range 0.0 PIx2 da)
       (map (partial a->xy r))
       (map (partial draw-dot dctx))
       (dorun)))

(defn draw-line [dctx [x1 y1] [x2 y2]]
  (doto dctx
    (.beginPath)
    (.moveTo x1 y1)
    (.lineTo x2 y2)
    (.stroke)))

(defn draw-lines [{:keys [dctx da r dots times]}]
  (aset dctx "stokeStyle" (u/rgb 192 32 32))
  (aset dctx "stokeWidth" 2)
  (doseq [n (range dots)]
    (draw-line dctx
               (->> n
                    (* da)
                    (a->xy r))
               (->> n
                    (* times)
                    (* da)
                    (a->xy r)))))


(defn redraw-times-table [this]
  (let [canvas (r/dom-node this)
        width (.-offsetWidth canvas)
        height (.-offsetHeight canvas)
        dctx (.getContext canvas "2d")
        {:keys [dots times]} (r/props this)
        ctx {:canvas canvas
             :dctx dctx
             :width width
             :height height
             :r (* (min width height) 0.45)
             :da (/ PIx2 dots)
             :dots dots
             :times times}]
    (doto ctx
      (start-drawing)
      (draw-border)
      (draw-dots)
      (draw-lines)
      (end-drawing))))

(defn- times-table [_]
  (r/create-class
    {:component-did-mount redraw-times-table
     :component-did-update redraw-times-table
     :render (constantly [:canvas])}))

(defn controls [dots on-change-dots times on-change-times]
  [:div.controls
   [:div.col.labels
    [:span "Dots:"]
    [:span "Times:"]]
   [:div.col.text
    [i/input-text dots on-change-dots]
    [i/input-text times on-change-times]]
   [:div.col.range
    [i/input-range min-dots max-dots 1 dots on-change-dots]
    [i/input-range min-times max-times 0.1 times on-change-times]]])

(defn set-dots [state]
  (fn [v]
    (some->> v
             (u/->num)
             (Math/round)
             (u/->bound min-dots max-dots)
             (swap! state assoc :dots))))

(defn set-times [state]
  (fn [v]
    (some->> v
             (u/->num)
             (u/->bound min-times max-times)
             (swap! state assoc :times))))

(defn page [state]
  (let [{:keys [dots times] :as curr-state} @state]
    [:div.page
     [controls dots (set-dots state) times (set-times state)]
     [times-table curr-state]]))

(defn reload []
  (r/render [page app-state] (.getElementById js/document "app")))

(defn ^:export main []
  (u/dev-setup)
  (reload))

