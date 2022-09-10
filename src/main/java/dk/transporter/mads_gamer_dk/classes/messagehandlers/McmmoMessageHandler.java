package dk.transporter.mads_gamer_dk.classes.messagehandlers;

import dk.transporter.mads_gamer_dk.mcmmo.Skills;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class McmmoMessageHandler implements IMessageHandler{

    private final Skills skills;

    public McmmoMessageHandler(Skills skills){
        this.skills = skills;
    }
    @Override
    public boolean messageReceived(String msg, String clean) {

        if(checkGigaDrill(clean) || checkSkillLevelIncreased(clean))
            return false;
        return false;


    }

    private boolean checkSkillLevelIncreased(String clean){
        final Pattern pattern = Pattern.compile("([a-zA-Z]+) skill increased by 1\\. Total \\(([^)]*)\\)");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            String numString = matcher.group(2).replace(",", "");
            Integer num = Integer.parseInt(numString);
            skills.setLevel(matcher.group(1), num);
            return true;
        }
        return false;
    }

    private boolean checkGigaDrill(String clean){
        if(clean.equals("**GIGA DRILL BREAKER ACTIVATED**")){
            skills.activatePowerUp("GIGA DRILL BREAKER");
            return true;
        }
        return false;
    }
}
