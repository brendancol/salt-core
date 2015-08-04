package com.uncharted.mosaic.generation.analytic.numeric

import com.uncharted.mosaic.generation.analytic.Aggregator

object CountAggregator extends Aggregator[Any, Double, java.lang.Double] {

  def default(): Double = {
    0D
  }

  override def add(current: Double, next: Option[Any]): Double = {
    current + 1
  }
  override def merge(left: Double, right: Double): Double = {
    left+right
  }

  def finish(intermediate: Double): java.lang.Double = {
    intermediate.toDouble
  }
}
