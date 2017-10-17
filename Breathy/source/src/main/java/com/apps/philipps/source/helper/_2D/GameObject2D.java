package com.apps.philipps.source.helper._2D;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Vector;

/**
 * Created by Jevgenij Huebert on 17.03.2017. Project Breathy
 */
public class GameObject2D extends Animated implements Cloneable {

    protected final String TAG = getClass().getSimpleName();
    public boolean intercectable = true;
    private View object;
    private double curRotation;
    public final long created = System.currentTimeMillis();

    public GameObject2D(Context context) {
        super(new Vector());
        object = new ImageView(context);
    }

    public GameObject2D(@NonNull View object) {
        this(object, null, null);
    }

    public GameObject2D(@NonNull View object, @NonNull Vector position) {
        this(object, position, position.clone());
    }


    public GameObject2D(@NonNull View object, Vector position, Vector destination) {
        this(object, position, destination, 0, false);
    }

    public GameObject2D(@NonNull View object, Vector position, Vector destination, double speed, boolean active) {
        super(position, destination, speed, active);
        this.object = object;
        if (this.position == null)
            this.position = new Vector(this.object.getX(), this.object.getY());
        else {
            object.setX(this.position.getF(0));
            object.setY(this.position.getF(1));
        }
        if (this.destination == null)
            this.destination = this.position.clone();
    }

    public GameObject2D(@NonNull View object, @NonNull GameObject2D gameObject) {
        this(object, gameObject.getPosition(), gameObject.getDestination());
        setSpeed(gameObject.getSpeed());
        if(gameObject.isMoving())
            move();
        curRotation = gameObject.curRotation;
    }

    public View getView() {
        return object;
    }


    @Override
    public void update(double deltaMilliseconds) {
        super.update(deltaMilliseconds);
        object.setX(position.getF(0));
        object.setY(position.getF(1));
    }

    @Override
    public void setPosition(Vector position) {
        super.setPosition(position);
        object.setX(position.getF(0));
        object.setY(position.getF(1));
    }

    public void setRotation(double alpha) {
        curRotation += alpha;
        //Animation rotationAnimation = new RotateAnimation(0.0f, -90.0f, 0.5f, 0.5f);
        //rotationAnimation.setDuration(1000);
        //rotationAnimation.setFillAfter(true);
        //rotationAnimation.setInterpolator(new LinearInterpolator());
        //getView().startAnimation( rotationAnimation );
        calcNewTarget(alpha);
        getView().setRotation((float) curRotation);

    }

    /**
     * Sets the View object
     *
     * @param object the View represented by this class
     */
    public void setObject(View object) {
        this.object = object;
    }

    public void calcNewTarget(double alpha) {

        float x = destination.getF(0);
        float y = destination.getF(1);
        float xD = position.getF(0);
        float yD = position.getF(1);
        float newX = (float) (xD + (x - xD) * Math.cos(alpha) - (y - yD) * Math.sin(alpha));
        ;
        float newY = (float) (yD + (x - xD) * Math.sin(alpha) + (y - yD) * Math.cos(alpha));
        ;

        //ToDo dafür sorgen das Bild unter bestimmten umständen aus dem Screen verschwindet

        destination = new Vector(x, y);
    }

    /**
     * Get Boundaries in the form:<br>
     * [x1 x2 y1 y2] This Coordinates describes edges in this kind:
     * <pre>
     * x1,y1   x2,y1
     * x1,y2   x2,y2
     * </pre>
     * <p>
     * x1,y1 is the Position of this Game Object
     *
     * @return 4 Values that describes edges of a box
     */
    public Vector getBoundaries() {
        double[] pos = position.get();
        return new Vector(pos[0], pos[0] + object.getMeasuredWidth(), pos[1], pos[1] + object.getMeasuredHeight());
    }

    public boolean intersect(GameObject2D gameObject) {
        if (intercectable) {
            Vector b = getBoundaries();
            Vector bO = gameObject.getBoundaries();
            return bO.get(0) <= b.get(0) && b.get(0) <= bO.get(1) && bO.get(2) <= b.get(2) && b.get(2) <= bO.get(3) ||
                    bO.get(0) <= b.get(1) && b.get(1) <= bO.get(1) && bO.get(2) <= b.get(2) && b.get(2) <= bO.get(3) ||
                    bO.get(0) <= b.get(0) && b.get(0) <= bO.get(1) && bO.get(2) <= b.get(3) && b.get(3) <= bO.get(3) ||
                    bO.get(0) <= b.get(1) && b.get(1) <= bO.get(1) && bO.get(2) <= b.get(3) && b.get(3) <= bO.get(3);
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + super.toString();
    }

    @Override
    public GameObject2D clone() {
        return new GameObject2D(object, this);
    }
}
