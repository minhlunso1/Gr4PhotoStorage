package group4.gr4photostorage.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import group4.gr4photostorage.R;

/**
 * Created by Minh on 7/19/2016.
 */
public class AlertDialogHelper {
    public static void show(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.Close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void show(Context context, String message, final IDialogDo iDialogDo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.Close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                iDialogDo.doWhat();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void countDownProcess(final int countDown, final TextView tvCountDown, final IDialogDo iDialogDo, final Dialog dialog) {
        HandlerHelper.run(new HandlerHelper.IHandlerDo() {
            @Override
            public void doThis() {
                if (countDown == 1) {
                    iDialogDo.doWhat();
                    dialog.dismiss();
                } else {
                    tvCountDown.setText(String.valueOf(countDown - 1));
                    countDownProcess(countDown - 1, tvCountDown, iDialogDo, dialog);
                }
            }
        }, 1000);
    }

    public static void showWithGo(Context context, String message, final IDialogDo iDialogDo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.Close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.Continue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                iDialogDo.doWhat();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showWithFullCase(Context context, String message, String leftText, String rightText, final IDialogDo iDialogDoLeft, final IDialogDo iDialogDoRight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNegativeButton(leftText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iDialogDoLeft.doWhat();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(rightText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                iDialogDoRight.doWhat();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showWithFullCase(Context context, String title, String message, String leftText, String rightText, final IDialogDo iDialogDoLeft, final IDialogDo iDialogDoRight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(leftText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iDialogDoLeft.doWhat();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(rightText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                iDialogDoRight.doWhat();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface IDialogDo {
        void doWhat();
    }
}

