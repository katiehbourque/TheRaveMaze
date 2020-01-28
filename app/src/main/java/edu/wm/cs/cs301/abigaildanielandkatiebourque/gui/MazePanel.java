package edu.wm.cs.cs301.abigaildanielandkatiebourque.gui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;

public class MazePanel extends View {

    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Drawable backgroundBottom;
    private Drawable backgroundTop;



    /**
     * Constructor that creates and initializes the bitmap, canvas and paint
     * @param context
     */
    public MazePanel(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(Constants.VIEW_HEIGHT, Constants.VIEW_WIDTH, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        backgroundBottom = getResources().getDrawable(R.drawable.maze_bottom);
        backgroundTop = getResources().getDrawable(R.drawable.maze_bottom);

    }

    public MazePanel(Context context, AttributeSet attributes) {
        super(context, attributes);
        bitmap = Bitmap.createBitmap(Constants.VIEW_HEIGHT, Constants.VIEW_WIDTH, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        backgroundBottom = getResources().getDrawable(R.drawable.maze_bottom);
        backgroundTop = getResources().getDrawable(R.drawable.maze_bottom);

    }


    @Override
    public void onMeasure(int measureWidth, int measureHeight) {
        super.onMeasure(measureWidth, measureHeight);
        setMeasuredDimension(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
    }

    public void mazebackground(int left, int top, int right, int bottom) {
        backgroundBottom.setBounds(left, bottom / 2, right, bottom);
        backgroundBottom.draw(canvas);
        backgroundTop.setBounds(left, top, right, bottom / 2);
        backgroundTop.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }



        /**
         * Method to draw the buffer image on a graphics object that is
         * obtained from the superclass.
         * Warning: do not override getGraphics() or drawing might fail.
         */

        public void update() {
            invalidate();
            //paint(getGraphics());
        }

        /*
        /**
         * Draws the buffer image to the given graphics object.
         * This method is called when this panel should redraw itself.
         * The given graphics object is the one that actually shows
         * on the screen.
         */

        /*
        @Override
        public void paint(Graphics g) {
            if (null == g) {
                System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
            }
            else {
                g.drawImage(bufferImage,0,0,null);
            }
        }

         */

    /**
     * Obtains a graphics object that can be used for drawing.
     * This MazePanel object internally stores the graphics object
     * and will return the same graphics object over multiple method calls.
     * The graphics object acts like a notepad where all clients draw
     * on to store their contribution to the overall image that is to be
     * delivered later.
     * To make the drawing visible on screen, one needs to trigger
     * a call of the paint method, which happens
     * when calling the update method.
     * @return graphics object to draw on, null if impossible to obtain image
     */
        /*
        public Graphics getBufferGraphics() {
            // if necessary instantiate and store a graphics object for later use
            if (null == graphics) {
                if (null == bufferImage) {
                    bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
                    if (null == bufferImage)
                    {
                        System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
                        return null; // still no buffer image, give up
                    }
                }
                graphics = (Graphics2D) bufferImage.getGraphics();
                if (null == graphics) {
                    System.out.println("Error: creation of graphics for buffered image failed, presumedly container not displayable");
                }
                else {
                    // System.out.println("MazePanel: Using Rendering Hint");
                    // For drawing in FirstPersonDrawer, setting rendering hint
                    // became necessary when lines of polygons
                    // that were not horizontal or vertical looked ragged
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                }
            }
            return graphics;
        }


        /**
         * draws line from (x, y) to (x2, y2)
         * @param x position in maze
         * @param y position in maze
         * @param x2 position in maze
         * @param y2 position in maze
         */
    public void drawLine(int x, int y, int x2, int y2) {
        //graphics.drawLine(x, y, x2, y2);
        paint.setStrokeWidth(5);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    /**
     * sets the color by using components red, green, and blue
     * @param r amount of red
     * @param g amount of green
     * @param b amount of blue
     */
    public void setColor(int r, int g, int b) {
        //graphics.setColor(new Color(r, g, b));
        paint.setColor(Color.rgb(r, g, b));
    }

    /**
     * represents the amount of red, green, and blue in a color as an integer
     * @param r amount of red
     * @param g amount of green
     * @param b amount of blue
     * @return the color as an integer
     */
    public static int getRGB(int r, int g, int b) {
        //Color color = new Color(r, g, b);
        return Color.rgb(r, g, b);

    }

    /**
     * fills rectangle at position (x, y) with specified width and height
     * @param x position in maze
     * @param y position in maze
     * @param width of rectangle
     * @param height of rectanlge
     */
    public void fillRect(int x, int y, int width, int height) {
        //graphics.fillRect(x,  y, width, height);
        paint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect(x, y, width+x, height+y);
        canvas.drawRect(rect, paint);
    }

    /**
     * fills oval at position (x, y) with specified width and height
     * @param x position in maze
     * @param y position in maze
     * @param width of oval
     * @param height of oval
     */
    public void fillOval(int x, int y, int width, int height) {
        //graphics.fillOval(x, y, width, height);
        paint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(x, y, width+x, height+x);
        canvas.drawOval(oval, paint);
    }

    /**
     * fills polygon with specified number of points and x, y coordinates of the points
     * @param xps x points of polygon
     * @param yps y points of polygon
     * @param nPoints number of points polygon has
     */
    public void fillPolygon(int[] xps, int[] yps, int nPoints) {
        //graphics.fillPolygon(xps, yps, nPoints);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.reset();
        path.moveTo(xps[0], yps[0]);
        for (int i = 1; i < nPoints; i++) {
            path.lineTo(xps[i], yps[i]);
        }
        path.lineTo(xps[0], yps[0]);
        canvas.drawPath(path, paint);
    }

    /**
     * converts the sRGB representation of the color wanted into a an array
     * with 3 elements each for the reference of the componenets of the Color
     * class
     * @param col, the sRGB representation of the color
     * @return the array with the components of the color
     */

    public static int[] getColorComponents(int col) {
        int[] com = new int[3];
        com[0] = Color.red(col);
        com[1] = Color.green(col);
        com[2] = Color.blue(col);

        return com;
    }





}
