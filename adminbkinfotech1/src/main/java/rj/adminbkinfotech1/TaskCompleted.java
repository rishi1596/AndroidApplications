package rj.adminbkinfotech1;

/**
 * Created by jimeet29 on 23-12-2017.
 */

public interface TaskCompleted {
    void onTaskComplete(String result);

    void onTaskComplete(String[] result);
}
