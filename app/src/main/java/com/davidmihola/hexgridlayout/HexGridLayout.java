package com.davidmihola.hexgridlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

import java.util.List;

import timber.log.Timber;

/**
 * Created by dmta on 20.05.16.
 */
public class HexGridLayout extends ViewGroup {

    private static final List<Point> logicalPoints = TreePVector.<Point>empty()
            .plus(new Point(0, 1))
            .plus(new Point(1, 1))
            .plus(new Point(1, 0))
            .plus(new Point(0, -1))
            .plus(new Point(-1, -1))
            .plus(new Point(-1, 0));

    private static PVector<DoublePoint> points = TreePVector.<DoublePoint>empty();

    static {
        for (Point point : logicalPoints) {
            Double newX = point.x - point.y * 0.5;
            Double newY = point.y * Math.sqrt(3.0) / 2;
            points = points.plus(new DoublePoint(newX, newY));
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private static final class Point {
        public final Integer x;
        public final Integer y;

        private Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final class DoublePoint {
        public final Double x;
        public final Double y;

        private DoublePoint(Double x, Double y) {
            this.x = x;
            this.y = y;
        }
    }

    private View centerView;
    private int itemSize;
    private int halfItemSize;
    private PVector<View> itemViews = TreePVector.empty();

    private ItemClickListener itemClickListener = null;

    public HexGridLayout(Context context) {
        super(context);
    }

    public HexGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HexGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(final HexGridData hexGridData) {

        {
            centerView = LayoutInflater.from(getContext()).inflate(R.layout.hex_grid_item_center, this, false);

            centerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClicked(hexGridData.centerItem);
                    }
                }
            });
            addView(centerView);

            TextView textView = (TextView) centerView.findViewById(R.id.title);
            textView.setText(hexGridData.centerItem.title);
        }

        for (final Item item : hexGridData.otherItems) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.hex_grid_item_center, this, false);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClicked(item);
                    }
                }
            });
            addView(itemView);

            itemViews = itemViews.plus(itemView);

            TextView textView = (TextView) itemView.findViewById(R.id.title);
            textView.setText(item.title);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Timber.v("onMeasure(widthMode = %d, widthSize = %d, heightMode = %d, heigthSize = %d", widthMode, widthSize, heightMode, heightSize);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = widthSize;
        } else {
            //Be whatever you want
            width = widthSize;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = heightSize;
        } else {
            //Be whatever you want
            height = heightSize;
        }

        //MUST CALL THIS
        Timber.v("setMeasuredDimension(width = %d, height = %d)", width, height);
        setMeasuredDimension(width, height);

        itemSize = Math.min(width / 4, height / 4);
        halfItemSize = itemSize / 2;

        if (centerView != null) {
            measureChild(centerView,
                    MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
            );
        }

        for (View itemView : itemViews) {
            measureChild(itemView,
                    MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
            );
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);

        Timber.v("onLayout(changed = %b, left = %d, top = %d, right = %d, bottom = %d", changed, left, top, right, bottom);
        if (!changed) {
            return;
        }

        int centerX = (left + right) / 2;
        int centerY = (top + bottom) / 2;

        if (centerView != null) {
            int l = centerX - halfItemSize;
            int t = centerY - halfItemSize;
            int r = centerX + halfItemSize;
            int b = centerY + halfItemSize;
            Timber.v("centerView.layout(l = %d, t = %d, r = %d, b = %d", l, t, r, b);

            centerView.layout(l, t, r, b);
        }

        for (int i = 0; i < itemViews.size(); i++) {
            DoublePoint center = points.get(i);

            int l = centerX + new Double(center.x * itemSize).intValue() - halfItemSize;
            int t = centerY + new Double(center.y * itemSize).intValue() - halfItemSize;
            int r = centerX + new Double(center.x * itemSize).intValue() + halfItemSize;
            int b = centerY + new Double(center.y * itemSize).intValue() + halfItemSize;

            itemViews.get(i).layout(l, t, r, b);
        }
    }
}
