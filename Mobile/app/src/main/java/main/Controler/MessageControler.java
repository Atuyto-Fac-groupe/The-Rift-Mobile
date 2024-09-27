package main.Controler;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.google.gson.Gson;
import main.App;
import main.Model.Message;
import main.View.FragmentMessage;
import main.View.MainActivity;

public class MessageControler implements View.OnTouchListener {

    private EditText editText;
    private Gson gson;
    private FragmentMessage fragmentMessage;
    public MessageControler(EditText editText, FragmentMessage fragmentMessage) {
        this.editText = editText;
        this.fragmentMessage = fragmentMessage;
        gson = new Gson();

        this.updateMessageRead();

    }

    private void updateMessageRead(){
        for (Message message : App.player.getMessages()){
            message.setSee(true);
        }
        MainActivity.getInstance().setBadgeMessageOnRead();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {

                senMessage();
                Log.d("idMessage", "message en cours ...");

                return true;
            }
        }
        return false;
    }

    private void senMessage() {
        String message = editText.getText().toString();
        if (!message.isEmpty()) {
            Message m = new Message();
            m.setMessage(message);
            m.setFrom("2");
            m.setTo("1");
            m.setSee(true);
            String json = gson.toJson(m);
            App.socketManager.sendMessage(json);
            editText.setText("");
        }
    }

}
