package bzlis.matrixmagi;

import android.graphics.Color;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.bzlis.matrixmagi.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class DataBag {

    private static DataBag instance = new DataBag();
    private HashSet<EditGridLayout> editList;
    private PixelGridView currView;
    private int A;
    private int B;
    private boolean arithOp;
    protected AdView adView;
    public boolean boardOut;
    public boolean itut;
    public boolean deltut;
    private GridLayout board;
    private MatrixElement chosen;
    public Vibrator vibes;
    public LinearLayout deletor;
    public boolean tutOut;

    private DataBag(){
        editList = new HashSet<>();
        arithOp = false;
        boardOut = false;
        deltut = true;
        tutOut = false;
    }

    public static DataBag getInstance(){
        return instance;
    }

    public void addData(EditGridLayout edit){
        editList.add(edit);
    }

    public EditGridLayout getData(int secret) {
        EditGridLayout retVal = null;
        Iterator<EditGridLayout> itr = editList.iterator();
        while (itr.hasNext()) {
            if ((retVal = itr.next()).getSecret() == secret)
                break;
        }
        return retVal;
    }

    public void removeData(EditGridLayout edit){
        Iterator<EditGridLayout> itr = editList.iterator();
        while (itr.hasNext()){
            if (itr.next().equals(edit)) {
                itr.remove();
                break;
            }
        }
    }

    public void adLoader(AdRequest adRequest){
        adView.loadAd(adRequest);
    }

    public void setAdView(AdView adView){
        this.adView = adView;
    }

    public int isOccupied(int x0, int y0, int x1, int y1, int secret, boolean actual){
        int occupied = -1;
        for (EditGridLayout edit : this.editList){
            if (edit.getSecret() != secret) {
                float x2, y2, x3, y3;
                if (!actual) {
                    x2 = edit.getActualX();
                    y2 = edit.getActualY();
                    x3 = x2 + edit.getNumCols() * edit.getCellLength();
                    y3 = y2 + edit.getNumRows() * edit.getCellLength();
                } else {
                    x2 = edit.getX();
                    y2 = edit.getY();
                    y3 = y2 + edit.getCellLength()*(edit.getNumRows()+2*edit.getThickness());
                    x3 = x2 + edit.getCellLength()*(edit.getNumCols()+2*edit.getThickness());
                }
                if (!((y1 < y2) || (x1 < x2) || (y3 < y0) || (x3 < x0))) {
                    if (!((y1 == y2) || (x1 == x2) || (y0 == y3) || (x0 == x3))) {
                        occupied = actual ? edit.getSecret() : 1;
                        break;
                    }
                }
            }
        }
        return occupied;
    }

    public HashSet<EditGridLayout> getData() {
        return editList;
    }

    public void setCurrView(PixelGridView px){
        this.currView = px;
        makeBoard();
    }

    public void makeBoard(){
        getCurrView().makeDim();
        if (board != null)
            ((ViewGroup)DataBag.getInstance().getCurrView().getParent()).removeView(board);
        board = new GridLayout(getCurrView().getContext());
        board.setLayoutParams(new RelativeLayout.LayoutParams(getCurrView().getWidth(), (int)Math.round(getCurrView().getHeight()/3.0)));
        board.setBackgroundResource(R.drawable.button_dark_gradient);
        board.setVisibility(View.GONE);
        int w = (int)Math.round(getCurrView().getWidth()/4.0);
        int h = (int)Math.round(getCurrView().getHeight()/12.0);
        String[] text = new String[]{"7","8","9","C","4","5","6","+","1","2","3","-","i","0",".",getCurrView().getResources().getString(R.string.next)};
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        text[14] = Character.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()).toString();
        for (int z = 0; z < 16; z++){
            Button digit = new Button(getCurrView().getContext());
            if (z != 15)
                digit.setBackgroundResource(R.drawable.button_light);
            else
                digit.setBackgroundResource(R.drawable.button_next);
            digit.setText(text[z]);
            digit.setTextSize(15);
            digit.setAllCaps(false);
            digit.setTextColor(Color.DKGRAY);
            if (z == 3)
                digit.setTextColor(Color.rgb(255, 128, 128));
            else if (z == 7 || z == 11)
                digit.setTextColor(Color.rgb(35, 188, 196));
            else if (z == 12)
                digit.setTextColor(Color.rgb(188, 66, 244));
            else if (z == 15)
                digit.setTextColor(Color.WHITE);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.rowSpec = GridLayout.spec(z/4);
            param.columnSpec = GridLayout.spec(z%4);
            param.setGravity(Gravity.CENTER);
            param.width = w;
            param.height = h;
            digit.setLayoutParams(param);
            board.addView(digit);
        }
        board.setTranslationY(Math.round(2*getCurrView().getHeight()/3.0));
        ((ViewGroup)DataBag.getInstance().getCurrView().getParent()).addView(board);
    }

    public PixelGridView getCurrView(){
        return currView;
    }

    public int getA(){
        return A;
    }

    public int getB(){
        return B;
    }

    public boolean getArithOp(){
        return arithOp;
    }

    public void setArithOp(boolean arithOp){
        this.arithOp = arithOp;
    }

    public void cleanData(RelativeLayout layout){
        for (EditGridLayout edit : editList){
            try {
                ((ViewGroup) edit.getParent()).removeView(edit);
                edit.cellLength = currView.cellLength;
            } catch (NullPointerException e) {}
           // layout.addView(edit);
        }
    }

    public void queueOp(int a, int b){
        this.A = a;
        this.B = b;
    }

    public void snapToGrid(){
      for (EditGridLayout edit : editList) {
        edit.setX(currView.cellLength * Math.round((edit.getX() + currView.cellLength) / currView.cellLength) - currView.cellLength * edit.thick);
        edit.setY(currView.cellLength * Math.round((edit.getY() + currView.cellLength) / currView.cellLength) - currView.cellLength * edit.thick);
        }
    }
    public void showBoard(final MatrixElement m){
        board.setVisibility(View.VISIBLE);
        board.bringToFront();
        m.setCursorVisible(true);
        boolean last = false;
        if (m.getNext() == null)
            last = true;
        for (int i = 0; i < board.getChildCount(); i++) {
            if (last && ((Button)board.getChildAt(i)).getText().equals(getCurrView().getResources().getString(R.string.next)))
                ((Button)board.getChildAt(i)).setText(getCurrView().getResources().getString(R.string.done));
            if (!last && ((Button)board.getChildAt(i)).getText().equals(getCurrView().getResources().getString(R.string.done)))
                ((Button)board.getChildAt(i)).setText(getCurrView().getResources().getString(R.string.next));
            board.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBag.getInstance().vibes.vibrate(20);
                    String text = ((Button)view).getText().toString();
                    if (text.equals("C"))
                        m.setText("");
                    else if (text.equals(getCurrView().getResources().getString(R.string.next))) {
                        DataBag.getInstance().requestSelected(m.getNext());
                        DataBag.getInstance().showBoard(m.getNext());
                    }
                    else if (text.equals(getCurrView().getResources().getString(R.string.done))) {
                        EditGridLayout.hideKeyboard();
                    }
                    else
                        m.setText(m.getText().toString() + text);
                }
            });
        }
    }

    public void requestSelected(MatrixElement m){
        if (chosen != null)
            chosen.setBackground(null);
        chosen = m;
        chosen.setBackgroundColor(Color.LTGRAY);
        ((EditGridLayout)chosen.getParent().getParent()).blare();
    }

    public void setVibrator(Vibrator vibes){
        this.vibes = vibes;
    }

    public void hideBoard(){
        if (chosen != null){
            ((EditGridLayout)chosen.getParent().getParent()).blare();
            chosen.setBackground(null);
            chosen = null;
        }
        board.setVisibility(View.GONE);
    }
}