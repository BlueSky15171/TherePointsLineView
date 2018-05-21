package com.ifootage.cn.therepointline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Description:
 * @anthor：Sky
 * @email：admin@itskylin.com
 * @time: 2018/04/24 13:29.
 */

public class TherePointsLineView extends View implements View.OnTouchListener {


	private float height;
	private float width;

	private Paint point;
	private Paint paint;
	private PointF center;
	private PointF p1;
	private float p11;
	private PointF p2;

	public TherePointsLineView(Context context) {
		super(context);
	}

	public TherePointsLineView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TherePointsLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void init(Context context, AttributeSet attrs) {
		this.setOnTouchListener(this);


		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		paint.setTextSize(40);

		point = new Paint();
		point.setAntiAlias(true);
		point.setColor(Color.BLACK);
		point.setStrokeWidth(3);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
		this.height = h;
		float lx = w / 2 - 100;
		float ly = h / 2 + 100;
		float rx = w / 2 + 100;
		float ry = h / 2 - 100;

		float cx = this.width / 2;
		float cy = this.height / 2;
		center = new PointF(cx, cy);
		p1 = new PointF(lx, ly);
		p2 = new PointF(rx, ry);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawLine(center.x, center.y, p1.x, p1.y, point);
		canvas.drawLine(center.x, center.y, p2.x, p2.y, point);
		canvas.drawCircle(center.x, center.y, 20, point);
		canvas.drawCircle(p1.x, p1.y, 20, paint);
//		canvas.drawText("p1", p1.x + 30, p1.y, paint);
		canvas.drawCircle(p2.x, p2.y, 20, paint);
//		canvas.drawText("p2", p2.x + 30, p2.y, paint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				float touchx = event.getX();
				if (touchx > p1.x) {
					p11 = (float) Math.sqrt((p2.x - center.x) * (p2.x - center.x) + (p2.y - center.y) * (p2.y - center.y));
				} else {
					p11 = (float) Math.sqrt((p1.x - center.x) * (p1.x - center.x) + (p1.y - center.y) * (p1.y - center.y));
				}
				Log.d("calcPoint", "onTouch: touch p11 = " + p11);
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				Log.i("calcPoint", "onTouch:touch x = " + x + "  , y = " + y);
				if (isRange(x, y)) {
					this.p1 = new PointF(x, y);
					this.p2 = calcNewPoint(this.center, this.p1, p11);
					postInvalidate();
					return true;
				}
				break;
		}
		return true;
	}

	public static PointF calcNewPoint(PointF center, PointF p, float r) {
		float tanA = (center.x - p.x) / (center.y - p.y);
		float rx = (float) ((tanA * r) / Math.sqrt(tanA * tanA + 1));
		float ry = (float) (r / Math.sqrt(tanA * tanA + 1));
		Log.i("calcPoint", String.format("onTouch: p2 =(x:%f,y:%f)", rx, ry));
		if (p.y <= center.y)
			return new PointF(center.x + rx, center.y + ry);
		else
			return new PointF(center.x - rx, center.y - ry);
	}

	/**
	 * 获取p1到p2的线段的长度
	 *
	 * @return
	 */
	public double getVectorLength(PointF vector) {
		return Math.sqrt(vector.x * vector.x + vector.y * vector.y);
	}

	private boolean isRange(float x, float y) {
		if (x <= this.width && y <= this.height) {
			return true;
		}
		return false;
	}
}
