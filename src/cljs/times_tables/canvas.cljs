(ns times-tables.canvas
  (:require [reagent.core :as r]
            [times-tables.util :as u]))

(def PIx2 (* Math/PI 2.0))
(def background (u/rgb 58 58 58))

(defn a->xy [r d]
  [(* r (Math/cos d))
   (* r (Math/sin d))])

(defn clean-area [{:keys [canvas dctx width height]}]
  (doto canvas
    (aset "width" width)
    (aset "height" height))
  (doto dctx
    (aset "fillStyle" background)
    (.fillRect 0 0 width height)))

(defn start-drawing [{:keys [dctx width height]}]
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

(defn draw-dots [{:keys [dctx r dots]}]
  (aset dctx "fillStyle" (u/rgb 192 32 32))
  (->> (range 0.0 PIx2 (/ PIx2 (:value dots)))
       (map (partial a->xy r))
       (map (partial draw-dot dctx))
       (dorun)))

(defn draw-line [dctx [x1 y1] [x2 y2]]
  (doto dctx
    (.beginPath)
    (.moveTo x1 y1)
    (.lineTo x2 y2)
    (.stroke)))

(defn draw-lines [{:keys [dctx r dots times]}]
  (aset dctx "stokeStyle" (u/rgb 192 32 32))
  (aset dctx "stokeWidth" 2)
  (let [dots-value (:value dots)
        times-value (:value times)
        da (/ PIx2 dots-value)]
    (doseq [n (range dots-value)]
      (draw-line dctx
                 (->> n
                      (* da)
                      (a->xy r))
                 (->> n
                      (* times-value)
                      (* da)
                      (a->xy r))))))

(defn redraw-times-table [this]
  (let [canvas (r/dom-node this)
        width (.-offsetWidth canvas)
        height (.-offsetHeight canvas)
        dctx (.getContext canvas "2d")
        ctx (merge (r/props this)
                   {:canvas canvas
                    :width width
                    :height height
                    :dctx dctx
                    :r (* (min width height) 0.45)})]
    (doto ctx
      (clean-area)
      (start-drawing)
      (draw-border)
      (draw-dots)
      (draw-lines)
      (end-drawing))))

(defn init-times-table [this]
  (.addEventListener js/window "resize" (fn [_]
                                          (redraw-times-table this)))
  (doto (r/dom-node this)
    (.addEventListener "touchstart" (fn [e]
                                      (.preventDefault e)
                                      (let [t (-> e .-touches (aget 0))
                                            sx (.-clientX t)
                                            sy (.-clientY t)
                                            left? (-> e .-target .-offsetWidth (/ 2.0) (> sx))
                                            target (if left?
                                                     (:dots (r/props this))
                                                     (:times (r/props this)))]
                                        (swap! (r/state-atom this) assoc
                                               :interaction? true
                                               :init-value (:value target)
                                               :set-value (:set-value target)
                                               :y sy
                                               :sy sy)
                                        (redraw-times-table this))))
    (.addEventListener "touchmove" (fn [e]
                                     (.preventDefault e)
                                     (let [y (-> e .-touches (aget 0) .-clientY)
                                           {:keys [init-value set-value sy]} (r/state this)]
                                       (set-value (->> y (- sy) (* 0.1) (+ init-value)))
                                       (swap! (r/state-atom this) assoc :y y)
                                       (redraw-times-table this))))
    (.addEventListener "touchend" (fn [e]
                                    (.preventDefault e)
                                    (swap! (r/state-atom this) assoc
                                           :interaction? false)
                                    (redraw-times-table this)))
    (.addEventListener "touchcancel" (fn [e]
                                       (.preventDefault e)
                                       (swap! (r/state-atom this) assoc
                                              :interaction? false)
                                       (redraw-times-table this))))
  (redraw-times-table this))

(defn- times-table [_]
  (r/create-class
    {:get-initial-state (constantly {})
     :component-did-mount init-times-table
     :component-did-update redraw-times-table
     :render (constantly [:canvas])}))
