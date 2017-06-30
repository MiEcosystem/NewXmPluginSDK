package com.xiaomi.smarthome.device.api;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by swx on 10/13/15.
 * Status and Column name
 */
public class DownloadConstants {

    /**
     * An identifier for a particular download, unique across the system.
     * Clients use this ID to make subsequent calls related to the download.
     */
    public final static String COLUMN_ID = BaseColumns._ID;

    /**
     * The client-supplied title for this download. This will be displayed in
     * system notifications. Defaults to the empty string.
     */
    public final static String COLUMN_TITLE = "title";

    /**
     * The client-supplied description of this download. This will be displayed
     * in system notifications. Defaults to the empty string.
     */
    public final static String COLUMN_DESCRIPTION = "description";

    /**
     * URI to be downloaded.
     */
    public final static String COLUMN_URI = "uri";

    /**
     * Internet Media Type of the downloaded file. If no value is provided upon
     * creation, this will initially be null and will be filled in based on the
     * server's response once the download has started.
     *
     * @see <a href="http://www.ietf.org/rfc/rfc1590.txt">RFC 1590, defining
     * Media Types</a>
     */
    public final static String COLUMN_MEDIA_TYPE = "media_type";

    /**
     * Total size of the download in bytes. This will initially be -1 and will
     * be filled in once the download starts.
     */
    public final static String COLUMN_TOTAL_SIZE_BYTES = "total_size";

    /**
     * Uri where downloaded file will be stored. If a destination is supplied by
     * client, that URI will be used here. Otherwise, the value will initially
     * be null and will be filled in with a generated URI once the download has
     * started.
     */
    public final static String COLUMN_LOCAL_URI = "local_uri";

    /**
     * Current status of the download, as one of the STATUS_* constants.
     */
    public final static String COLUMN_STATUS = "status";

    /**
     * Provides more detail on the status of the download. Its meaning depends
     * on the value of {@link #COLUMN_STATUS}.
     * <p/>
     * When {@link #COLUMN_STATUS} is {@link #STATUS_FAILED}, this indicates the
     * type of error that occurred. If an HTTP error occurred, this will hold
     * the HTTP status code as defined in RFC 2616. Otherwise, it will hold one
     * of the ERROR_* constants.
     * <p/>
     * When {@link #COLUMN_STATUS} is {@link #STATUS_PAUSED}, this indicates why
     * the download is paused. It will hold one of the PAUSED_* constants.
     * <p/>
     * If {@link #COLUMN_STATUS} is neither {@link #STATUS_FAILED} nor
     * {@link #STATUS_PAUSED}, this column's value is undefined.
     *
     * @see <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6.1.1">RFC
     * 2616 status codes</a>
     */
    public final static String COLUMN_REASON = "reason";

    /**
     * Number of bytes download so far.
     */
    public final static String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";

    /**
     * Timestamp when the download was last modified, in
     * {@link System#currentTimeMillis System.currentTimeMillis()} (wall clock
     * time in UTC).
     */
    public final static String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";

    /**
     * Value of {@link #COLUMN_STATUS} when the download is waiting to start.
     */
    public final static int STATUS_PENDING = 1;

    /**
     * Value of {@link #COLUMN_STATUS} when the download is currently running.
     */
    public final static int STATUS_RUNNING = 1 << 1;

    /**
     * Value of {@link #COLUMN_STATUS} when the download is waiting to retry or
     * resume.
     */
    public final static int STATUS_PAUSED = 1 << 2;

    /**
     * Value of {@link #COLUMN_STATUS} when the download has successfully
     * completed.
     */
    public final static int STATUS_SUCCESSFUL = 1 << 3;

    /**
     * Value of {@link #COLUMN_STATUS} when the download has failed (and will
     * not be retried).
     */
    public final static int STATUS_FAILED = 1 << 4;
    /**
     * DownloadProvider authority
     */
    public static final String AUTHORITY = "com.xiaomi.smarthome.downloads";

    /**
     * The content:// URI for the data table in the provider
     *
     * @hide
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/my_downloads");

    /**
     * The content URI for accessing all downloads across all UIDs (requires the
     * ACCESS_ALL_DOWNLOADS permission).
     */
    public static final Uri ALL_DOWNLOADS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/all_downloads");

}
