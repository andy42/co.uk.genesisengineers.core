package ui.view;

import content.Context;
import drawable.Drawable;
import drawable.DrawableManager;
import input.KeyEvent;
import ui.util.AttributeParser;
import ui.util.AttributeSet;
import input.MotionEvent;
import util.CollisionBox;
import util.Logger;
import util.Vector2Df;

public class View {
    protected int id = -1;
    protected Vector2Df dimensions = new Vector2Df(0, 0);
    protected Vector2Df position = new Vector2Df(0, 0);
    protected Drawable background = null;
    protected Drawable backgroundFocused = null;
    protected Drawable backgroundPressed = null;
    protected ViewGroup.LayoutParams layoutParams;
    protected int measuredWidth = 0;
    protected int measuredHeight = 0;
    protected int visibility = VISIBLE;

    protected int topMargin = 0;
    protected int bottomMargin = 0;
    protected int leftMargin = 0;
    protected int rightMargin = 0;

    protected int topPadding = 0;
    protected int bottomPadding = 0;
    protected int leftPadding = 0;
    protected int rightPadding = 0;

    protected boolean isFocused = false;
    protected boolean isFocusable = false;

    protected boolean isClickable = false;

    protected boolean isPressed = false;

    public final static int VISIBLE = 0;
    public final static int INVISIBLE = 1;
    public final static int GONE = 2;

    protected ViewGroup parent = null;

    Context context;

    public View (Context context, AttributeSet attrs) {
        this.id = AttributeParser.getInteger(attrs, "id", -1);

        this.background = DrawableManager.getInstance().getDrawable(
                AttributeParser.getInteger(attrs, "background", null));
        this.backgroundPressed = DrawableManager.getInstance().getDrawable(
                AttributeParser.getInteger(attrs, "background_pressed", null));
        this.backgroundFocused =DrawableManager.getInstance().getDrawable(
                AttributeParser.getInteger(attrs, "background_focused", null));


        this.topMargin = AttributeParser.getDimension(attrs, "margin_top", 0);
        this.bottomMargin = AttributeParser.getDimension(attrs, "margin_bottom", 0);
        this.leftMargin = AttributeParser.getDimension(attrs, "margin_left", 0);
        this.rightMargin = AttributeParser.getDimension(attrs, "margin_right", 0);

        this.topPadding = AttributeParser.getDimension(attrs, "padding_top", 0);
        this.bottomPadding = AttributeParser.getDimension(attrs, "padding_bottom", 0);
        this.leftPadding = AttributeParser.getDimension(attrs, "padding_left", 0);
        this.rightPadding = AttributeParser.getDimension(attrs, "padding_right", 0);

        this.isClickable = AttributeParser.getBoolean(attrs, "clickable", false);


        this.context = context;
    }

    public View findViewById(int id){
        if(this.id == id){
            return this;
        }
        else return null;
    }

    public View () {
    }

    public Integer getId () {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vector2Df getDimensions () {
        return this.dimensions;
    }

    public void setDimensions (Vector2Df dimensions) {
        this.dimensions = dimensions;
    }

    public Vector2Df getPosition () {
        return this.position;
    }

    public void setPosition (Vector2Df position) {
        this.position = position;
    }

    public Drawable getBackground () {
        return this.background;
    }

    public void setBackground (Drawable background) {
        this.background = background;
    }

    public void setLayoutParams (ViewGroup.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    public ViewGroup.LayoutParams getLayoutParams () {
        return this.layoutParams;
    }

    public int getTopMargin () {
        return topMargin;
    }

    public int getBottomMargin () {
        return bottomMargin;
    }

    public int getLeftMargin () {
        return leftMargin;
    }

    public int getRightMargin () {
        return rightMargin;
    }

    public int getTopPadding () {
        return topPadding;
    }

    public int getBottomPadding () {
        return bottomPadding;
    }

    public int getLeftPadding () {
        return leftPadding;
    }

    public int getRightPadding () {
        return rightPadding;
    }

    public int getVisibility () {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Context getContext () {
        return context;
    }

    public void renderBackgound(){

        if(this.backgroundPressed != null && this.isPressed){
            this.backgroundPressed.draw(position, dimensions, 0);
        }
        else if(this.backgroundFocused != null && this.isFocused ){
            this.backgroundFocused.draw(position, dimensions, 0);
        }
        else if (background != null) {
            background.draw(position, dimensions, 0);
        }
    }

    public void render () {
        if(visibility != VISIBLE) return;
        renderBackgound();
    }

    protected int getSize (int value, int parent) {
        if (value == ViewGroup.LayoutParams.MATCH_PARENT) {
            return parent;
        }
        if (value > 0) {
            return value;
        } else {
            return 0;
        }
    }

    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int width = getSize(layoutParams.width, widthMeasureSpec);
        int height = getSize(layoutParams.height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    public void onLayout (int width, int height, int x, int y) {
        this.dimensions.x = width;
        this.dimensions.y = height;
        this.position.x = x;
        this.position.y = y;
    }

    protected void setMeasuredDimension (int width, int height) {
        this.measuredWidth = width;
        this.measuredHeight = height;
    }

    public int getMeasuredWidth () {
        return this.measuredWidth;
    }

    public int getMeasuredHeight () {
        return this.measuredHeight;
    }

    public void setParent(ViewGroup parent){
        this.parent = parent;
    }

    public ViewGroup getParent() {
        return parent;
    }

    public void removeParent(){
        this.parent = null;
    }

    public boolean hasFocus(){
        return isFocused;
    }

    public boolean canTakeFocus(){
        if(this.visibility != VISIBLE){
            return false;
        }
        return this.isFocusable;
    }

    public void onFocusChanged(boolean isFocused){
        this.isFocused = isFocused;
    }

    public void unFocus(){
        if(this.isFocused){
            onFocusChanged(false);
        }
    }

    public void handleFocusGainInternal(){
        if(parent == null){
            return;
        }
        onFocusChanged(true);
        Logger.info("handleFocusGainInternal");
        parent.requestChildFocus(this, this);
    }

    public Boolean dispatchKeyEvent (KeyEvent keyEvent){
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        if(getListenerInfo().mOnTouchListener != null){
            getListenerInfo().mOnTouchListener.OnTouch(motionEvent, this);
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            if(this.isClickable) {
                this.isPressed = true;
            }
            if(this.canTakeFocus()){
                this.handleFocusGainInternal();
            }
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL && this.isClickable){

            if(motionEvent.getAction() == MotionEvent.ACTION_UP && this.isPressed){
                if(getListenerInfo().mOnClickListener != null){
                    getListenerInfo().mOnClickListener.onClick(this);
                }
            }
            this.isPressed = false;
        }
        return this.isClickable;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent){

        CollisionBox collisionBox = new CollisionBox();
        collisionBox.initTopLeft(this.position, this.dimensions);

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            if(collisionBox.pointCollisionTest(motionEvent.getPosition())){
                return onTouchEvent(motionEvent);
            }
            else {
                return false;
            }
        } else {
            return onTouchEvent(motionEvent);
        }
    }

    public void setClickable (boolean clickable) {
        this.isClickable = clickable;
    }

    public void setOnClickListener(OnClickListener listener) {
        if (!this.isClickable) {
            setClickable(true);
        }
        getListenerInfo().mOnClickListener = listener;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        if (!this.isClickable) {
            setClickable(true);
        }
        getListenerInfo().mOnTouchListener = listener;
    }

    protected ListenerInfo listenerInfo = new ListenerInfo();

    public ListenerInfo getListenerInfo(){
        return this.listenerInfo;
    }

    static class ListenerInfo {
        public OnClickListener mOnClickListener;
        public OnTouchListener mOnTouchListener;
    }

    public interface OnClickListener {
        void onClick(View v);
    }
    public interface OnTouchListener{
        void OnTouch(MotionEvent event, View v);
    }
}
