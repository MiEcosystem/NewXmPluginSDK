package com.xiaomi.smarthome.device.api.printer;

import android.print.PrinterInfo;

import com.xiaomi.smarthome.device.api.DeviceStat;

import java.util.List;

/**
 * 用这个类直接调用打印机 PrinterService 的api，绕过系统的打印服务，在实现类中new的 PrinterService。内部的实现思路可以参考app module下的 com.xiaomi.smarthome.printer.PrinterControlImpl
 *
 */
public interface PrinterControl {

    /**
     * 创建printerservice，当activity onCreate时候调用
     *
     * @param device
     */
    public void onCreate(DeviceStat device);

    /**
     * 销毁printerservice，当activity onDestroy时候调用
     */
    public void onDestroy();

    /**
     * 开始获取打印机参数
     *
     * @param listener 获取到的打印机参数会回调到这里，如果在 获取参数的时间内 发现了新的打印机也会回调这个方法，注意判断是否是当前 did 的打印机。
     */
    public void onStartPrinterStateTracking(OnAddPrinterListener listener);

    /**
     * 停止获取参数 时候调用
     */
    public void onStopPrinterStateTracking();

    /**
     * 添加一个打印任务
     *
     * @param job 需要实现一个PrintJobProxy 的子类，包含 PrintJob 中的各个方法。
     */
    public void onPrintJobQueued(PrintJobProxy job);

    /**
     * 取消打印任务
     */
    public void onRequestCancelPrintJob(PrintJobProxy job);

    public interface OnAddPrinterListener {
        /**
         * 添加打印机的回调
         *
         * @param printers 会返回所有添加的打印机，需要过滤是否是需要的PrinterInfo。
         */
        void onAddPrinters(List<PrinterInfo> printers);
    }
}
