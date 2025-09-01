package com.example.medarix.listaAjuste;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.medarix.models.Recordatorio;

import java.util.Calendar;

public class ReminderScheduler {

    // Programar un recordatorio
    public static void programar(Context context, Recordatorio r) {
        // Creamos un intent hacia el Receiver
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("title", r.titulo);
        intent.putExtra("time", r.horaFormateada());

        // Calculamos la hora exacta
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, r.hora);
        cal.set(Calendar.MINUTE, r.minuto);
        cal.set(Calendar.SECOND, 0);

        // Si la hora ya pasó hoy, lo movemos al día siguiente
        if (cal.before(Calendar.getInstance())) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        // PendingIntent con flags correctos
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pi = PendingIntent.getBroadcast(context, r.hashCode(), intent, flags);

        // Obtenemos AlarmManager
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Comprobamos permisos en Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            return; // no podemos programar
        }

        // Programamos la alarma
        am.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                pi
        );
    }

    // Cancelar un recordatorio
    public static void cancelar(Context context, Recordatorio r) {
        Intent intent = new Intent(context, ReminderReceiver.class);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pi = PendingIntent.getBroadcast(context, r.hashCode(), intent, flags);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }
}
