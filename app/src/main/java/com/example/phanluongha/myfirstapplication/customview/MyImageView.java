package com.example.phanluongha.myfirstapplication.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.DetailExhibitionActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.model.MapNode;

import java.io.Console;
import java.util.ArrayList;

import okhttp3.MultipartBody;

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
    public int first = -1;
    public int second = -1;
    public ZoomView zoomView;

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        iconStoreNormal = BitmapFactory.decodeResource(getResources(), R.drawable.icon_marker_nomal);
        iconStoreNormalWidth = iconStoreNormal.getWidth();
        iconStoreNormalHeight = iconStoreNormal.getHeight();

        iconStoreActive = BitmapFactory.decodeResource(getResources(), R.drawable.icon_marker_active);
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
        paintLine.setStrokeWidth(3);
    }

    public void setZoomView(ZoomView zoomView) {
        this.zoomView = zoomView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float zoom = 1.0f;
        if (zoomView != null)
            zoom = zoomView.getZoom();
        int iconStoreNormalWidthNew = (int) (iconStoreNormalWidth / zoom);
        int iconStoreNormalHeightNew = (int) (iconStoreNormalHeight / zoom);
        int iconStoreActiveWidthNew = (int) (iconStoreActiveWidth / zoom);
        int iconStoreActiveHeightNew = (int) (iconStoreActiveHeight / zoom);
        for (int i = 0; i < mapNodes.size(); i++) {
            MapNode mn = mapNodes.get(i);
            if (mn.isStore()) {
                if (!mn.isActive()) {
                    float x = mn.getX() - iconStoreNormalWidthNew / 2;
                    float y = mn.getY() - iconStoreNormalHeightNew;
                    canvas.drawBitmap(Bitmap.createScaledBitmap(iconStoreNormal, iconStoreNormalWidthNew, iconStoreNormalHeightNew, false), x, y, paintNormal);
                } else {
                    float x = mn.getX() - iconStoreActiveWidthNew / 2;
                    float y = mn.getY() - iconStoreActiveHeightNew;
                    canvas.drawBitmap(Bitmap.createScaledBitmap(iconStoreActive, iconStoreActiveWidthNew, iconStoreActiveHeightNew, false), x, y, paintNormal);
                }
            }
        }
        if (first != -1 && second != -1) {
            dijkstra(first, second, mapNodes.size(), arrayNode, canvas);
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
                float zoom = 1.0f;
                if (zoomView != null)
                    zoom = zoomView.getZoom();
                int iconStoreNormalWidthNew = (int) (iconStoreNormalWidth / zoom);
                int iconStoreNormalHeightNew = (int) (iconStoreNormalHeight / zoom);
                int iconStoreActiveWidthNew = (int) (iconStoreActiveWidth / zoom);
                int iconStoreActiveHeightNew = (int) (iconStoreActiveHeight / zoom);
                for (int i = 0; i < mapNodes.size(); i++) {
                    MapNode mn = mapNodes.get(i);
                    float mnX;
                    float mnY;
                    if (mn.isActive()) {
                        mnX = mn.getX() - iconStoreNormalWidthNew / 2;
                        mnY = mn.getY() - iconStoreNormalHeightNew;
                        if (x >= mnX && x <= mnX + iconStoreNormalWidthNew && y >= mnY && y <= mnY + iconStoreNormalHeightNew) {
//                            mn.setActive(false);
//                            if (first == i) {
//                                first = -1;
//                                if (second != -1) {
//                                    first = second;
//                                    second = -1;
//                                }
//                            }
//                            invalidate();
                            showInfor(mn);
                            break;
                        }
                    } else {
                        mnX = mn.getX() - iconStoreActiveWidthNew / 2;
                        mnY = mn.getY() - iconStoreActiveHeightNew;
                        if (x >= mnX && x <= mnX + iconStoreActiveWidthNew && y >= mnY && y <= mnY + iconStoreActiveHeightNew) {
//                            mn.setActive(true);
//                            if (first == -1) {
//                                first = i;
//                            } else if (second == -1) {
//                                second = i;
//                            } else {
//                                mapNodes.get(first).setActive(false);
//                                mapNodes.get(second).setActive(false);
//                                first = i;
//                                second = -1;
//                            }
//                            invalidate();
                            showInfor(mn);
                            break;
                        }
                    }

                }
                return true;
        }
        return false;
    }

    private void showInfor(final MapNode mn) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(context
                .getString(R.string.exhibitor));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            alertDialogBuilder.setMessage(Html.fromHtml("<p>" + mn.getName() + "<p></p>" + mn.getBooth() + "</p>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            alertDialogBuilder.setMessage(Html.fromHtml("<p>" + mn.getName() + "<p></p>" + mn.getBooth() + "</p>"));
        }
        alertDialogBuilder
                .setPositiveButton(
                        context.getString(R.string.go_to_booth),
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {
                                Intent exhibitor = new Intent(context, DetailExhibitionActivity.class);
                                exhibitor.putExtra("id", mn.getId());
                                context.startActivity(exhibitor);
                            }
                        })

                .setNegativeButton(
                        context.getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {

                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder
                .create();
        alertDialog.show();

    }

}
