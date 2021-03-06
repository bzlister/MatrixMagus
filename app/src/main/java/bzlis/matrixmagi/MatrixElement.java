package bzlis.matrixmagi;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.View;

public class MatrixElement extends android.support.v7.widget.AppCompatEditText {


    private MatrixElement next;
    private Context context;
    protected ComplexForm trueValue;
    private String excepMessage = "";

    public MatrixElement(Context context){
        super(context);
        this.context = context;
        this.setInputType(InputType.TYPE_NULL);
        this.setBackground(null);
        this.setTypeface(Typeface.SERIF, Typeface.ITALIC);
        this.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.setPadding(0, 0, 0, 5);
      //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.setMargins(0, 0, 0, -4);
       // this.setLayoutParams(params);
    }

    public void setNext(MatrixElement next){
        this.next = next;
    }

    public MatrixElement getNext(){
        return this.next;
    }

    public ComplexForm getTrueValue(){
        return this.trueValue;
    }

    public void setTrueValue(ComplexForm cf){
        this.trueValue = cf;
    }

    public void setExcepMessage(String s){
        this.excepMessage = s;
    }

    public String getExcepMessage(){
        return excepMessage;
    }
}
