package threatarrest.monitoring.editing;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/***
 * Check the metadata of a text file in order to verify
 * if the file has been modified
 */
public abstract class AbFileWatcher extends TimerTask {
    private long timeStamp;
    private File file;
    private Timer timer;
    private String msg;

    public AbFileWatcher(File file ) {
        this.file = file;
        this.timeStamp = file.lastModified();
    }
    @Override
    public void run() {
        long timeStamp = file.lastModified();

        if( this.timeStamp != timeStamp ) {
            this.timeStamp = timeStamp;
            onChange(file);
        }
    }
    public String getMsg(){
        return msg;
    }
    protected abstract void onChange( File file );
}
