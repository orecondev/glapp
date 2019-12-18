package com.github.orecondev.glapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OpenGlES20Android extends Activity implements GLSurfaceView.Renderer
{
    private GLSurfaceView SurfaceView;
    double allWidth;
    double allHeight;
    TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SurfaceView = new GLSurfaceView(this);
        FrameLayout baseLay = new FrameLayout(this);
        setContentView(baseLay);
        baseLay.addView(SurfaceView);

        Point p = getDisplaySize(this);
        allHeight = p.y;
        allWidth = p.x;

        //aaa


        tv = new TextView(this);
        tv.setText(allHeight+"");
        tv.setTextColor(Color.parseColor("#ff00ff"));
        baseLay.addView(tv);

        TextView tvv = new TextView(this);
        tvv.setText(allWidth+"");
        tvv.setTranslationX(400);
        tvv.setWidth(100);
        tvv.setHeight(200);
        tvv.setTextColor(Color.parseColor("#ff00ff"));
        baseLay.addView(tvv);


        final Button rbutton = new Button(this);
        rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                rbuttonClick();
            }
        });

        rbutton.setText("right");
        rbutton.setGravity(2);
        rbutton.setTranslationX((int)(0.8*allWidth));
        rbutton.setTranslationY((int)(0.8*allHeight));
        baseLay.addView(rbutton, 3,new FrameLayout.LayoutParams(200,250));
        SurfaceView.setEGLContextClientVersion(2);
        SurfaceView.setRenderer(this);




    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SurfaceView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SurfaceView.onResume();
    }

    private int Texture, Program, VertexBufferObject;

    private int[] Location = new int[3];

    private float[] Model = new float[16];
    private float[] View = new float[16];
    private float[] ModelView = new float[16];
    private float[] Projection = new float[16];
    private float[] ModelViewProjection = new float[16];

    private float[]  idou ={
            1,0,0,0.1f,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1,
    };

    private static float Angle = 0.0f;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
        Texture = LoadTexture2D(R.raw.texture);

        Program = CreateProgram(R.raw.texturing_vs, R.raw.texturing_fs);

        Location[0] = GLES20.glGetUniformLocation(Program, "ModelViewProjection");
        Location[1] = GLES20.glGetAttribLocation(Program, "vert_TexCoord");
        Location[2] = GLES20.glGetAttribLocation(Program, "vert_Position");

        float data[] = { // s, t, x, y, z
                0.0f, 0.0f, -0.5f, -0.5f, -0.5f,
                1.0f, 0.0f, -0.5f, -0.5f,  0.5f,
                1.0f, 1.0f, -0.5f,  0.5f,  0.5f,
                1.0f, 1.0f, -0.5f,  0.5f,  0.5f,
                0.0f, 1.0f, -0.5f,  0.5f, -0.5f,
                0.0f, 0.0f, -0.5f, -0.5f, -0.5f,

                0.0f, 0.0f,  0.5f, -0.5f,  0.5f,
                1.0f, 0.0f,  0.5f, -0.5f, -0.5f,
                1.0f, 1.0f,  0.5f,  0.5f, -0.5f,
                1.0f, 1.0f,  0.5f,  0.5f, -0.5f,
                0.0f, 1.0f,  0.5f,  0.5f,  0.5f,
                0.0f, 0.0f,  0.5f, -0.5f,  0.5f,

                0.0f, 0.0f, -0.5f, -0.5f, -0.5f,
                1.0f, 0.0f,  0.5f, -0.5f, -0.5f,
                1.0f, 1.0f,  0.5f, -0.5f,  0.5f,
                1.0f, 1.0f,  0.5f, -0.5f,  0.5f,
                0.0f, 1.0f, -0.5f, -0.5f,  0.5f,
                0.0f, 0.0f, -0.5f, -0.5f, -0.5f,

                0.0f, 0.0f, -0.5f,  0.5f,  0.5f,
                1.0f, 0.0f,  0.5f,  0.5f,  0.5f,
                1.0f, 1.0f,  0.5f,  0.5f, -0.5f,
                1.0f, 1.0f,  0.5f,  0.5f, -0.5f,
                0.0f, 1.0f, -0.5f,  0.5f, -0.5f,
                0.0f, 0.0f, -0.5f,  0.5f,  0.5f,

                0.0f, 0.0f,  0.5f, -0.5f, -0.5f,
                1.0f, 0.0f, -0.5f, -0.5f, -0.5f,
                1.0f, 1.0f, -0.5f,  0.5f, -0.5f,
                1.0f, 1.0f, -0.5f,  0.5f, -0.5f,
                0.0f, 1.0f,  0.5f,  0.5f, -0.5f,
                0.0f, 0.0f,  0.5f, -0.5f, -0.5f,

                0.0f, 0.0f, -0.5f, -0.5f,  0.5f,
                1.0f, 0.0f,  0.5f, -0.5f,  0.5f,
                1.0f, 1.0f,  0.5f,  0.5f,  0.5f,
                1.0f, 1.0f,  0.5f,  0.5f,  0.5f,
                0.0f, 1.0f, -0.5f,  0.5f,  0.5f,
                0.0f, 0.0f, -0.5f, -0.5f,  0.5f,
        };

        VertexBufferObject = CreateVertexBufferObject(data);

        Matrix.setLookAtM(View, 0, 0.0f, 0.0f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        Matrix.setIdentityM(Model, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        //Matrix.rotateM(Model, 0, Angle, 0.0f, 1.0f, 0.0f);
        //Matrix.rotateM(Model, 0, Angle, 1.0f, 0.0f, 0.0f);
        float[] aa = {0,0,0};
        //Matrix.transpose(Model,0,idou,0);
        Matrix.translateM(Model,0,0,0,0);
        //Matrix.multiplyMM(Model, 0, Model, 0, idou, 0);

        Angle += 0.25f;

        Matrix.multiplyMM(ModelView, 0, View, 0, Model, 0);
        Matrix.multiplyMM(ModelViewProjection, 0, Projection, 0, ModelView, 0);

        GLES20.glUseProgram(Program);
        GLES20.glUniformMatrix4fv(Location[0], 1, false, ModelViewProjection, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VertexBufferObject);

        GLES20.glEnableVertexAttribArray(Location[1]);
        GLES20.glVertexAttribPointer(Location[1], 2, GLES20.GL_FLOAT, false, 20, 0);

        GLES20.glEnableVertexAttribArray(Location[2]);
        GLES20.glVertexAttribPointer(Location[2], 3, GLES20.GL_FLOAT, false, 20, 8);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,  Texture);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,  0);

        GLES20.glDisableVertexAttribArray(Location[2]);
        GLES20.glDisableVertexAttribArray(Location[1]);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glUseProgram(0);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);

        Matrix.perspectiveM(Projection, 0, 45.0f, (float)width / (float)height, 0.125f, 512.0f);
    }

    private int GetMaxAnisotropy()
    {
        int[] maxAnisotropy = new int[1];

        GLES20.glGetIntegerv(GLES11Ext.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy, 0);

        return maxAnisotropy[0];
    }

    private int LoadTexture2D(int resourceId)
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);

        if(texture[0] == 0)
        {
            throw new RuntimeException("\nError creating texture (" + resourceId + ")!");
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);

        if(bitmap == null)
        {
            throw new RuntimeException("\nError loading texture (" + resourceId + ")!");
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES11Ext.GL_TEXTURE_MAX_ANISOTROPY_EXT, GetMaxAnisotropy());

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        bitmap.recycle();

        return texture[0];
    }

    private String LoadShaderSource(int resourceId)
    {
        InputStream inputStream = getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String string;
        StringBuilder stringBuilder = new StringBuilder();

        try
        {
            while((string = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(string);
                stringBuilder.append('\n');
            }
            //aaa
        }
        catch(IOException e)
        {
            return null;
        }

        return stringBuilder.toString();
    }

    private int CreateShader(int type, int resourceId)
    {
        int shader = GLES20.glCreateShader(type);

        if(shader == 0)
        {
            throw new RuntimeException("\nError creating shader (" + resourceId + ")!");
        }

        GLES20.glShaderSource(shader, LoadShaderSource(resourceId));
        GLES20.glCompileShader(shader);

        int[] compileStatus = new int[1];

        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if(compileStatus[0] == GLES20.GL_FALSE)
        {
            String info = GLES20.glGetShaderInfoLog(shader);

            GLES20.glDeleteShader(shader);

            throw new RuntimeException("\nError compiling shader (" + resourceId + "):\n" + info);
        }

        return shader;
    }

    private int CreateProgram(int vertexShaderResourceId, int fragmentShaderResourceId)
    {
        int vertexShader = CreateShader(GLES20.GL_VERTEX_SHADER, vertexShaderResourceId);

        int fragmentShader = CreateShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderResourceId);

        int program = GLES20.glCreateProgram();

        if(program == 0)
        {
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);

            throw new RuntimeException("\nError creating program (" + vertexShaderResourceId + ", " + fragmentShaderResourceId + ")!");
        }

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        int[] linkStatus = new int[1];

        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if(linkStatus[0] == GLES20.GL_FALSE)
        {
            String info = GLES20.glGetProgramInfoLog(program);

            GLES20.glDetachShader(program, vertexShader);
            GLES20.glDetachShader(program, fragmentShader);
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);
            GLES20.glDeleteProgram(program);

            throw new RuntimeException("\nError linking program (" + vertexShaderResourceId + ", " + fragmentShaderResourceId + "):\n" + info);
        }

        return program;
    }

    private int CreateVertexBufferObject(float[] data)
    {
        int[] vertexBufferObject = new int[1];

        GLES20.glGenBuffers(1,  vertexBufferObject, 0);

        if(vertexBufferObject[0] == 0)
        {
            throw new RuntimeException("\nError creating vertex buffer object!");
        }

        FloatBuffer buffer = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(data).position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        return vertexBufferObject[0];
    }


    public static Point getDisplaySize(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    public boolean rbuttonClick(){
        Matrix.translateM(Model,0,0.1f,0.1f,0);

        tv.setText("おされたー");
        return true;

    }


}



/*

gabege
        lineLay.addView(tv,
            new LinearLayout.LayoutParams
                    (10,
                            10)
    );

        lineLay.addView(ttv,
                new LinearLayout.LayoutParams
                        (20,
                               20)
        );

* */