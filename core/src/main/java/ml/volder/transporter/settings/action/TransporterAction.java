package ml.volder.transporter.settings.action;

import net.labymod.api.client.gui.screen.widget.action.Selectable;
import net.labymod.api.client.gui.screen.widget.action.SliderInteraction;
import net.labymod.api.client.gui.screen.widget.action.Switchable;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public class TransporterAction<T> implements DropdownWidget.ChangeListener<T>, Consumer<T>, Selectable<T>, SliderInteraction, Switchable {
    private final Class<T> actionType;
    private Consumer<T> consumer;
    private Consumer<Float> floatConsumer;
    private Consumer<Boolean> booleanConsumer;

    @SuppressWarnings("unchecked")
    private boolean isBooleanConsumer(Consumer<?> consumer) {
        try {
            Consumer<Boolean> booleanConsumer = (Consumer<Boolean>) consumer;
            return true;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isFloatConsumer(Consumer<?> consumer) {
        try {
            Consumer<Float> floatConsumer = (Consumer<Float>) consumer;
            return true;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    private Class<?> getTypeParameter(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] typeArgs = paramType.getActualTypeArguments();
            if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                return (Class<?>) typeArgs[0];
            }
        }
        return Object.class;  // Default to Object if the type parameter cannot be determined
    }

    @SuppressWarnings("unchecked")
    public TransporterAction(Consumer<T> consumer) {
        this.actionType = (Class<T>) getTypeParameter(getClass());
        this.consumer = consumer;
        if(isBooleanConsumer(consumer)) {
            this.booleanConsumer = (Consumer<Boolean>) consumer;
            this.floatConsumer = null;
        } else if(isFloatConsumer(consumer)) {
            this.floatConsumer = (Consumer<Float>) consumer;
            this.booleanConsumer = null;
        } else {
            this.floatConsumer = null;
            this.booleanConsumer = null;
        }
    }

    public void setInitialValue(T initialValue) {
        consumer.accept(initialValue);
    }
    @SuppressWarnings("unchecked")
    public void andAfter(Consumer<? super T> after) {
        this.consumer = consumer.andThen(after);
        if(booleanConsumer != null && isBooleanConsumer(after)) {
            this.booleanConsumer = this.booleanConsumer.andThen((Consumer<Boolean>) after);
        } else if(floatConsumer != null && isFloatConsumer(after)) {
            this.floatConsumer = this.floatConsumer.andThen((Consumer<Float>) after);
        }
    }

    public Class<T> getActionType() {
        return actionType;
    }


    @Override
    public void onChange(T t) {
        if(consumer != null) {
            consumer.accept(t);
        }
    }

    @Override
    public void accept(T t) {
        if(consumer != null) {
            consumer.accept(t);
        }
    }

    @Override
    public void select(T t) {
        if(consumer != null) {
            consumer.accept(t);
        }
    }

    @Override
    public void updateValue(float v) {
        if(floatConsumer != null) {
            floatConsumer.accept(v);
        }
    }

    @Override
    public void switchValue(boolean b) {
        if(booleanConsumer != null) {
            booleanConsumer.accept(b);
        }
    }
}
