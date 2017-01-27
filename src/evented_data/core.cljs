(ns evented_data.core
  (:require [evented_data.protocols :as protocols
             :refer [diff noun events]]))


(defn emap [k v & kvs]
  (apply assoc (protocols/EventMap. {} [] []) k v kvs))


(defn evec [x & xs]
  (apply conj (protocols/EventVector. [] [] []) x xs))


