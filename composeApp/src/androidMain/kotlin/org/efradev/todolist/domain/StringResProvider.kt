package org.efradev.todolist.domain

import android.content.Context
import org.koin.core.context.GlobalContext

actual fun provideStringResProvider(): StringResProvider = { key ->
    val context = GlobalContext.get().get<Context>()
    val resId = context.resources.getIdentifier(key, "string", context.packageName)
    if (resId != 0) context.getString(resId) else key
}
