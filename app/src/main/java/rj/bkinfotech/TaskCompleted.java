package rj.bkinfotech;

/**
 * Created by jimeet29 on 23-12-2017.
 */

public interface TaskCompleted {
    public void onTaskComplete(String result);

    void onTaskComplete(String[] result);
}
