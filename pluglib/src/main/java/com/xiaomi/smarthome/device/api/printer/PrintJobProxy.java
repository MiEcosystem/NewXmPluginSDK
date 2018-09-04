package com.xiaomi.smarthome.device.api.printer;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.print.PrintDocumentInfo;
import android.print.PrintJobId;
import android.print.PrintJobInfo;
import android.printservice.PrintDocument;
import android.printservice.PrintJob;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class PrintJobProxy {
    private PrintJob printJob;

    public PrintJobProxy() {
    }

    public PrintJobProxy(PrintJob printJob) {
        this.printJob = printJob;
    }

    public PrintJobId getId() {
        return printJob.getId();
    }

    public PrintJobInfo getInfo() {
        return printJob.getInfo();
    }

    public ParcelFileDescriptor getDocumentData() {
        PrintDocument document = printJob.getDocument();
        if (document == null) {
            return null;
        } else {
            return document.getData();
        }
    }

    public PrintDocumentInfo getDocumentInfo() {
        PrintDocument document = printJob.getDocument();
        if (document == null) {
            return null;
        } else {
            return document.getInfo();
        }
    }

    public boolean isQueued() {
        return printJob.isQueued();
    }

    public boolean isStarted() {
        return printJob.isStarted();
    }

    public boolean isBlocked() {
        return printJob.isBlocked();
    }

    public boolean isCompleted() {
        return printJob.isCompleted();
    }

    public boolean isFailed() {
        return printJob.isFailed();
    }

    public boolean isCancelled() {
        return printJob.isCancelled();
    }

    public boolean start() {
        return printJob.start();
    }

    public boolean block(String reason) {
        return printJob.block(reason);
    }

    public boolean complete() {
        return printJob.complete();
    }

    public boolean fail(String error) {
        return printJob.fail(error);
    }

    public boolean cancel() {
        return printJob.cancel();
    }

    public boolean setTag(String tag) {
        return printJob.setTag(tag);
    }

    public String getTag() {
        return printJob.getTag();
    }

    public String getAdvancedStringOption(String key) {
        return printJob.getAdvancedStringOption(key);
    }

    public boolean hasAdvancedOption(String key) {
        return printJob.hasAdvancedOption(key);
    }

    public int getAdvancedIntOption(String key) {
        return printJob.getAdvancedIntOption(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PrintJobProxy) {
            if (printJob == null) {
                return ((PrintJobProxy) obj).printJob == null;
            } else {
                return printJob.equals(((PrintJobProxy) obj).printJob);
            }
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return printJob.hashCode();
    }

}
