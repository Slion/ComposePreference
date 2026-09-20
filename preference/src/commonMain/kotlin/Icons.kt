/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.zhanghai.compose.preference

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal object Icons {
    val Info: ImageVector
        get() {
            if (_info != null) {
                return _info!!
            }
            _info =
                materialIcon(name = "Outlined.Info") {
                    materialPath {
                        moveTo(11.0f, 7.0f)
                        horizontalLineToRelative(2.0f)
                        verticalLineToRelative(2.0f)
                        horizontalLineToRelative(-2.0f)
                        close()
                        moveTo(11.0f, 11.0f)
                        horizontalLineToRelative(2.0f)
                        verticalLineToRelative(6.0f)
                        horizontalLineToRelative(-2.0f)
                        close()
                        moveTo(12.0f, 2.0f)
                        curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
                        reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
                        reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
                        reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
                        close()
                        moveTo(12.0f, 20.0f)
                        curveToRelative(-4.41f, 0.0f, -8.0f, -3.59f, -8.0f, -8.0f)
                        reflectiveCurveToRelative(3.59f, -8.0f, 8.0f, -8.0f)
                        reflectiveCurveToRelative(8.0f, 3.59f, 8.0f, 8.0f)
                        reflectiveCurveToRelative(-3.59f, 8.0f, -8.0f, 8.0f)
                        close()
                    }
                }
            return _info!!
        }

    private var _info: ImageVector? = null

    val Search: ImageVector
        get() {
            if (_search != null) {
                return _search!!
            }
            _search =
                materialIcon(name = "Filled.Search") {
                    materialPath {
                        // The standard Material "search" glyph (magnifier with a full handle to
                        // the bottom-right corner).
                        moveTo(15.5f, 14.0f)
                        horizontalLineToRelative(-0.79f)
                        lineToRelative(-0.28f, -0.27f)
                        curveTo(15.41f, 12.59f, 16.0f, 11.11f, 16.0f, 9.5f)
                        curveTo(16.0f, 5.91f, 13.09f, 3.0f, 9.5f, 3.0f)
                        curveTo(5.91f, 3.0f, 3.0f, 5.91f, 3.0f, 9.5f)
                        curveTo(3.0f, 13.09f, 5.91f, 16.0f, 9.5f, 16.0f)
                        curveTo(11.11f, 16.0f, 12.59f, 15.41f, 13.73f, 14.43f)
                        lineToRelative(0.27f, 0.28f)
                        verticalLineToRelative(0.79f)
                        lineToRelative(4.99f, 4.99f)
                        lineTo(20.49f, 19.49f)
                        lineTo(15.5f, 14.0f)
                        close()
                        moveTo(9.5f, 14.0f)
                        curveTo(7.01f, 14.0f, 5.0f, 11.99f, 5.0f, 9.5f)
                        curveTo(5.0f, 7.01f, 7.01f, 5.0f, 9.5f, 5.0f)
                        curveTo(11.99f, 5.0f, 14.0f, 7.01f, 14.0f, 9.5f)
                        curveTo(14.0f, 11.99f, 11.99f, 14.0f, 9.5f, 14.0f)
                        close()
                    }
                }
            return _search!!
        }

    private var _search: ImageVector? = null

    val Close: ImageVector
        get() {
            if (_close != null) {
                return _close!!
            }
            _close =
                materialIcon(name = "Filled.Close") {
                    materialPath {
                        moveTo(19.0f, 6.41f)
                        lineTo(17.59f, 5.0f)
                        lineTo(12.0f, 10.59f)
                        lineTo(6.41f, 5.0f)
                        lineTo(5.0f, 6.41f)
                        lineTo(10.59f, 12.0f)
                        lineTo(5.0f, 17.59f)
                        lineTo(6.41f, 19.0f)
                        lineTo(12.0f, 13.41f)
                        lineTo(17.59f, 19.0f)
                        lineTo(19.0f, 17.59f)
                        lineTo(13.41f, 12.0f)
                        close()
                    }
                }
            return _close!!
        }

    private var _close: ImageVector? = null

    val ArrowBack: ImageVector
        get() {
            if (_arrowBack != null) {
                return _arrowBack!!
            }
            _arrowBack =
                materialIcon(name = "AutoMirrored.Filled.ArrowBack", autoMirror = true) {
                    materialPath {
                        moveTo(20.0f, 11.0f)
                        lineTo(20.0f, 13.0f)
                        lineTo(8.0f, 13.0f)
                        lineTo(13.5f, 18.5f)
                        lineTo(12.08f, 19.92f)
                        lineTo(4.16f, 12.0f)
                        lineTo(12.08f, 4.08f)
                        lineTo(13.5f, 5.5f)
                        lineTo(8.0f, 11.0f)
                        close()
                    }
                }
            return _arrowBack!!
        }

    private var _arrowBack: ImageVector? = null
}

private inline fun materialIcon(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder,
): ImageVector =
    ImageVector.Builder(
            name = name,
            defaultWidth = MaterialIconDimension.dp,
            defaultHeight = MaterialIconDimension.dp,
            viewportWidth = MaterialIconDimension,
            viewportHeight = MaterialIconDimension,
            autoMirror = autoMirror,
        )
        .block()
        .build()

private inline fun ImageVector.Builder.materialPath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit,
) =
    path(
        fill = SolidColor(Color.Black),
        fillAlpha = fillAlpha,
        stroke = null,
        strokeAlpha = strokeAlpha,
        strokeLineWidth = 1f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1f,
        pathFillType = pathFillType,
        pathBuilder = pathBuilder,
    )

private const val MaterialIconDimension = 24f
