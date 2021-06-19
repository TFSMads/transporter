package dk.transporter.mads_gamer_dk.utils;

public class MessageHandler {

    public String putMessage;

    public String getMessage;

    public String putManglerMessage;

    public String getManglerMessage;

    public String delayMessage;

    public String getMessage(String message){
        if(message.equals("putMessage")){
            return this.putMessage;
        }else if(message.equals("getMessage")){
            return this.getMessage;
        }else if(message.equals("putManglerMessage")){
            return this.putManglerMessage;
        }else if(message.equals("getManglerMessage")){
            return this.getManglerMessage;
        }else if(message.equals("delayMessage")){
            return this.delayMessage;
        }
        return null;
    }

    public String getMessageById(Integer id){
        if(id == 0){
            return this.putMessage;
        }else if(id == 1){
            return this.getMessage;
        }else if(id == 2){
            return this.putManglerMessage;
        }else if(id == 3){
            return this.getManglerMessage;
        }else if(id == 4){
            return this.delayMessage;
        }
        return null;
    }

    public void setMessage(String message, String newMessage){
        if(message.equals("putMessage")){
            this.putMessage = newMessage;
        }else if(message.equals("getMessage")){
            this.getMessage = newMessage;
        }else if(message.equals("putManglerMessage")){
            this.putManglerMessage = newMessage;
        }else if(message.equals("getManglerMessage")){
            this.getManglerMessage = newMessage;
        }else if(message.equals("delayMessage")){
            this.delayMessage = newMessage;
        }
    }

    public void setMessageById(Integer id, String newMessage){
        if(id == 0){
            this.putMessage = newMessage;
        }else if(id == 1){
            this.getMessage = newMessage;
        }else if(id == 2){
            this.putManglerMessage = newMessage;
        }else if(id == 3){
            this.getManglerMessage = newMessage;
        }else if(id == 4){
            this.delayMessage = newMessage;
        }
    }


    public String getPutMessage(String item,Integer antal,Integer total){
        String tempMsg = putMessage;

        tempMsg = tempMsg.replace('&','§');

        tempMsg = tempMsg.replace("%item%", item);

        tempMsg = tempMsg.replace("%antal%", antal.toString());

        tempMsg = tempMsg.replace("%total%", total.toString());

        return tempMsg;

    }

    public String getGetMessage(String item,Integer antal,Integer total){
        String tempMsg = getMessage;

        tempMsg = tempMsg.replace('&','§');

        tempMsg = tempMsg.replace("%item%", item);

        tempMsg = tempMsg.replace("%antal%", antal.toString());

        tempMsg = tempMsg.replace("%total%", total.toString());

        return tempMsg;

    }

    public String getManglerPutMessage(String item){
        String tempMsg = putManglerMessage;

        tempMsg = tempMsg.replace('&','§');

        tempMsg = tempMsg.replace("%item%", item);

        return tempMsg;

    }

    public String getManglerGetMessage(String item){
        String tempMsg = getManglerMessage;

        tempMsg = tempMsg.replace('&','§');

        tempMsg = tempMsg.replace("%item%", item);

        return tempMsg;

    }

    public String getDelayMessage(){
        String tempMsg = delayMessage;

        tempMsg = tempMsg.replace('&','§');

        return tempMsg;
    }

}
