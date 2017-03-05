package com.example.phanluongha.myfirstapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.model.MapNode;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by PhanLuongHa on 2/25/2017.
 */

public class MyImageView extends ImageView {

    private Context context;
    private Paint paintNormal;
    private Paint paintActive;
    private Paint paintLine;
    private Bitmap iconStoreNormal;
    private int iconStoreNormalWidth;
    private int iconStoreNormalHeight;
    private Bitmap iconStoreActive;
    private int iconStoreActiveWidth;
    private int iconStoreActiveHeight;
    public ArrayList<MapNode> mapNodes = new ArrayList<MapNode>();
    public int[][] arrayNode;
    private int first = -1;
    private int second = -1;

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        iconStoreNormal = BitmapFactory.decodeResource(getResources(), R.drawable.icon_exhibitors);
        iconStoreNormalWidth = iconStoreNormal.getWidth();
        iconStoreNormalHeight = iconStoreNormal.getHeight();

        iconStoreActive = BitmapFactory.decodeResource(getResources(), R.drawable.icon_floorplan);
        iconStoreActiveWidth = iconStoreActive.getWidth();
        iconStoreActiveHeight = iconStoreActive.getHeight();

        paintNormal = new Paint();
        paintNormal.setAntiAlias(true);
        paintNormal.setFilterBitmap(true);
        paintNormal.setDither(true);

        paintActive = new Paint();
        paintActive.setAntiAlias(true);
        paintActive.setFilterBitmap(true);
        paintActive.setDither(true);

        paintLine = new Paint();
        paintLine.setColor(Color.BLUE);
        paintLine.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mapNodes.size(); i++) {
            MapNode mn = mapNodes.get(i);
            if (mn.isStore()) {
                if (!mn.isActive()) {
                    float x = mn.getX() - iconStoreNormalWidth / 2;
                    float y = mn.getY() - iconStoreNormalHeight / 2;
                    canvas.drawBitmap(iconStoreNormal, x, y, paintNormal);
                } else {
                    float x = mn.getX() - iconStoreActiveWidth / 2;
                    float y = mn.getY() - iconStoreActiveWidth / 2;
                    canvas.drawBitmap(iconStoreActive, x, y, paintNormal);
                }
            }
            if (first != -1 && second != -1) {
                dijkstra(first, second, mapNodes.size(), arrayNode, canvas);
            }
        }
    }

    private void dijkstra(int first, int second, int n, int G[][], Canvas canvas)

    {
        int sum = 0;
        int i;
        int S[] = new int[n];
        int Len[] = new int[n];
        int P[] = new int[n];


        for (i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                sum += G[i][j];
            }

        for (int k = 0; k < n; k++) {
            for (int j = 0; j < n; j++) {
                if (k != j && G[k][j] == 0)
                    G[k][j] = sum;
            }
        }


        for (int k = 0; k < n; k++) {
            Len[k] = sum;
            S[k] = 0;
            P[k] = first;
        }

        Len[first] = 0;
        while (S[second] == 0) {
            for (i = 0; i < n; i++)
                if (S[i] == 0 && Len[i] < sum)
                    break;
            if (i >= n) {
                Toast.makeText(context, "Can not find path", Toast.LENGTH_LONG).show();
                break;
            }

            for (int j = 0; j < n; j++) {
                if (S[j] == 0 && Len[i] > Len[j]) {
                    i = j;
                }
            }
            S[i] = 1;
            for (int j = 0; j < n; j++) {
                if (S[j] == 0 && Len[i] + G[i][j] < Len[j]) {
                    Len[j] = Len[i] + G[i][j];
                    P[j] = i;
                }
            }
        }

        if (Len[second] > 0 && Len[second] < sum) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            while (i != first) {
                path.add(i);
                i = P[i];
            }
            path.add(first);
            for (int j = 0; j < path.size() - 1; j++) {
                MapNode mnFirst = mapNodes.get(path.get(j));
                MapNode mnSecond = mapNodes.get(path.get(j + 1));
                canvas.drawLine(mnFirst.getX(), mnFirst.getY(), mnSecond.getX(), mnSecond.getY(), paintLine);
            }
        } else {
            Toast.makeText(context, "Can not find path", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < mapNodes.size(); i++) {
                    MapNode mn = mapNodes.get(i);
                    float mnX;
                    float mnY;
                    if (mn.isActive()) {
                        mnX = mn.getX() - iconStoreNormalWidth / 2;
                        mnY = mn.getY() - iconStoreNormalHeight / 2;
                        if (x >= mnX && x <= mnX + iconStoreNormalWidth && y >= mnY && y <= mnY + iconStoreNormalHeight) {
                            mn.setActive(false);
                            if (first == i) {
                                first = -1;
                                if (second != -1) {
                                    first = second;
                                    second = -1;
                                }
                            }
                            invalidate();
                            break;
                        }
                    } else {
                        mnX = mn.getX() - iconStoreActiveWidth / 2;
                        mnY = mn.getY() - iconStoreActiveHeight / 2;
                        if (x >= mnX && x <= mnX + iconStoreActiveWidth && y >= mnY && y <= mnY + iconStoreActiveHeight) {
                            mn.setActive(true);
                            if (first == -1) {
                                first = i;
                            } else if (second == -1) {
                                second = i;
                            } else {
                                mapNodes.get(first).setActive(false);
                                mapNodes.get(second).setActive(false);
                                first = i;
                                second = -1;
                            }
                            invalidate();
                            break;
                        }
                    }

                }
                return true;
        }
        return false;
    }

}
