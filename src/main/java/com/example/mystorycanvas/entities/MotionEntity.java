package com.example.mystorycanvas.entities;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mystorycanvas.layers.Layer;
import com.example.mystorycanvas.utils.MathUtils;

public abstract class MotionEntity {
    @NonNull
    protected final Layer layer;

    /**
     * transformation matrix for the entity
     */
    protected final Matrix matrix = new Matrix();
    /**
     * true - entity is selected and need to draw it's border
     * false - not selected, no need to draw it's border
     */
    private boolean isSelected;

    /**
     * maximum scale of the initial image, so that
     * the entity still fits within the parent canvas
     */
    protected float holyScale;

    /**
     * width of canvas the entity is drawn in
     */
    @IntRange(from = 0)
    protected int canvasWidth;
    /**
     * height of canvas the entity is drawn in
     */
    @IntRange(from = 0)
    protected int canvasHeight;

    /**
     * Destination points of the entity
     * 5 points. Size of array - 10; Starting upper left corner, clockwise
     * last point is the same as first to close the circle
     * NOTE: saved as a field variable in order to avoid creating array in draw()-like methods
     */
    private final float[] destPoints = new float[10]; // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    /**
     * Initial points of the entity
     *
     * @see #destPoints
     */
    protected final float[] srcPoints = new float[10];  // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0

    @NonNull
    private Paint borderPaint = new Paint();

    public MotionEntity(@NonNull Layer layer, boolean isSelected) {
        this.layer = layer;
        this.isSelected = isSelected;
    }

    public MotionEntity(@NonNull Layer layer,
                        int canvasWidth,
                        int canvasHeight) {
        this.layer = layer;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    private boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    protected void updateMatrix() {
        // init matrix to E - identity matrix
        matrix.reset();

        float topLeftX = layer.getX() * canvasWidth;
        float topLeftY = layer.getY() * canvasHeight;

        float centerX = topLeftX + getWidth() * holyScale * 0.5F;
        float centerY = topLeftY + getHeight() * holyScale * 0.5F;

        // calculate params
        float rotationInDegree = layer.getRotationInDegrees();
        float scaleX = layer.getScale();
        float scaleY = layer.getScale();
        if (layer.isFlipped()) {
            // flip (by X-coordinate) if needed
            rotationInDegree *= 1.0F;
            scaleX *= 1.0F;

            matrix.preScale(scaleX, scaleY, centerX, centerY);

            // rotate
            matrix.preRotate(rotationInDegree, centerX, centerY);

            // translate
            matrix.preTranslate(topLeftX, topLeftY);

            // applying holy scale - S`, the result will be : L = S * R * T * S`
            matrix.preScale(holyScale, holyScale);
        }
        else
        {
            rotationInDegree *= 1.0F;
            scaleX *= -1.0F;

            matrix.preScale(scaleX, scaleY, centerX, centerY);

            // rotate
            matrix.preRotate(rotationInDegree, centerX, centerY);

            // translate
            matrix.preTranslate(topLeftX, topLeftY);

            // applying holy scale - S`, the result will be : L = S * R * T * S`
            matrix.preScale(holyScale, holyScale);
        }
    }

    public float absoluteCenterX() {
        float topLeftX = layer.getX() * canvasWidth;
        return topLeftX + getWidth() * holyScale * 0.5F;
    }

    public float absoluteCenterY() {
        float topLeftY = layer.getY() * canvasHeight;

        return topLeftY + getHeight() * holyScale * 0.5F;
    }

    public PointF absoluteCenter() {
        float topLeftX = layer.getX() * canvasWidth;
        float topLeftY = layer.getY() * canvasHeight;

        float centerX = topLeftX + getWidth() * holyScale * 0.5F;
        float centerY = topLeftY + getHeight() * holyScale * 0.5F;

        return new PointF(centerX, centerY);
    }

    public void moveToCanvasCenter() {
        moveCenterTo(new PointF(canvasWidth / 2.0F, canvasHeight / 2.0F));
    }

    public void moveCenterTo(PointF moveToCenter) {
        PointF currentCenter = absoluteCenter();
        Log.d("tag",""+absoluteCenter()+"  this is center");
        layer.postTranslate((moveToCenter.x - currentCenter.x) / canvasWidth,
                (moveToCenter.y - currentCenter.y) / canvasHeight);
    }

    private final PointF pA = new PointF();
    private final PointF pB = new PointF();
    private final PointF pC = new PointF();
    private final PointF pD = new PointF();

    public boolean pointInLayerRect(PointF point) {

        updateMatrix();
        // map rect vertices
        matrix.mapPoints(destPoints, srcPoints);

        pA.x = destPoints[0];pA.y = destPoints[1];
        pB.x = destPoints[2];pB.y = destPoints[3];
        pC.x = destPoints[4];pC.y = destPoints[5];
        pD.x = destPoints[6];pD.y = destPoints[7];

        return MathUtils.pointInTriangle(point, pA, pB, pC) || MathUtils.pointInTriangle(point, pA, pD, pC);
    }

    public final void draw(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {

        updateMatrix();

        canvas.save();

        drawContent(canvas, drawingPaint);

        if (isSelected()) {
            // get alpha from drawingPaint
            int storedAlpha = borderPaint.getAlpha();
            if (drawingPaint != null) {
                borderPaint.setAlpha(drawingPaint.getAlpha());
            }
            drawSelectedBg(canvas);
            // restore border alpha
            borderPaint.setAlpha(storedAlpha);
        }
        canvas.restore();
    }

    private void drawSelectedBg(Canvas canvas) {
        matrix.mapPoints(destPoints, srcPoints);
        //noinspection Range
        canvas.drawLines(destPoints, 0, 8, borderPaint);
        //noinspection Range
        canvas.drawLines(destPoints, 2, 8, borderPaint);
    }

    @NonNull
    public Layer getLayer() {
        return layer;
    }

    public void setBorderPaint(@NonNull Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    protected abstract void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint);

    public abstract int getWidth();

    public abstract int getHeight();

    public void release() {
        // free resources here
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            //noinspection ThrowFromFinallyBlock
            super.finalize();
        }
    }
}
