/*
 * Copyright 2015 Uncharted Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package software.uncharted.salt.core.generation

import software.uncharted.salt.core.analytic.Aggregator
import software.uncharted.salt.core.projection.Projection
import software.uncharted.salt.core.generation.output.TileData
import software.uncharted.salt.core.generation.request.TileRequest
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import scala.reflect.ClassTag

/**
 * Represents a ValueExtractor -> Projection -> binAggregator -> tileAggregator
 *                            ValueExtractor --------^
 * Multiple series are meant to be tiled by a TileGenerator simultaneously
 *
 * @param maxBin The maximum possible bin index (i.e. if your tile is 256x256, this would be (255,255))
 * @param cExtractor a mechanism for grabbing the data-space coordinates from a source record
 * @param projection the  projection from data to some space (i.e. 2D or 1D)
 * @param vExtractor a mechanism for grabbing or synthesizing the "value" column from a source record (optional)
 * @param binAggregator the desired bin analytic strategy
 * @param tileAggregator the desired tile analytic strategy (optional)
 * @tparam RT the source data record type (the source data is an RDD[RT])
 * @tparam DC the abstract type representing a data-space coordinate
 * @tparam TC the abstract type representing a tile coordinate. Must feature a zero-arg constructor.
 * @tparam BC the abstract type representing a bin coordinate. Must feature a zero-arg
 *            constructor and should be something that can be represented in 1 dimension.
 * @tparam T Input data type for bin aggregators
 * @tparam U Intermediate data type for bin aggregators
 * @tparam V Output data type for bin aggregators, and input for tile aggregator
 * @tparam W Intermediate data type for tile aggregators
 * @tparam X Output data type for tile aggregators
 */
class Series[RT, DC, TC, BC, T, U, V, W, X](
  val maxBin: BC,
  val cExtractor: (RT) => Option[DC],
  val projection: Projection[DC,TC,BC],
  val vExtractor: Option[(RT) => Option[T]],
  val binAggregator: Aggregator[T, U, V],
  val tileAggregator: Option[Aggregator[V, W, X]]) extends Serializable
