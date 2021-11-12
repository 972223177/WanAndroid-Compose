package com.ly.wanandroid.utils

import android.app.Application
import android.os.Process
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * @author ly
 * 第三方SDK初始化工具 分异步和同步，按level初始化，值小的先
 * @use: InitTaskRunner(application , listOf(task1))
 *          .add(syncInitTask("task2"){ app ->
 *                  //同步初始化
 *           })
 *          .add(asyncInitTask("task3" , level = 1 , onlyMainProcess = true){ app ->
 *                  //异步初始化
 *          })
 *          add(object:InitTask{
 *             override val level:Int = 0
 *             override val taskName:String = "task4"
 *             override val onlyMainProcess:Boolean = true
 *             override val sync:Boolean = true
 *             override fun init(application:Application){
 *                  //...你的初始化代码
 *             }
 *          }).run()
 */
class InitTaskRunner(
    private val application: Application,
    tasks: List<InitTask> = emptyList()
) {

    private val mTasks = ArrayList(tasks)

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun add(task: InitTask): InitTaskRunner {
        mTasks.add(task)
        return this
    }

    fun run() {
        val isMainProcess = isMainProcess()
        val syncTasks = mutableListOf<InitTask>()
        val asyncTasks = mutableListOf<InitTask>()
        for (task in mTasks) {
            if (!isMainProcess && task.onlyMainProcess) {
                logE(TAG, "task:${task.taskName} only initial in mainProcess")
                continue
            }
            if (task.sync) {
                syncTasks.add(task)
            } else {
                asyncTasks.add(task)
            }
        }
        runSync(syncTasks)
        runAsync(asyncTasks)
    }

    private fun runSync(tasks: MutableList<InitTask>) {
        tasks.sortedBy { it.level }
        mCoroutineScope.launch(Dispatchers.Main.immediate) {
            tasks.forEach {
                val success = try {
                    it.init(application)
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
                (it.taskName to success).printlnInitialResult()
            }
        }
    }

    private fun runAsync(tasks: MutableList<InitTask>) {
        mCoroutineScope.launch(Dispatchers.Default + SupervisorJob()) {
            val levels = tasks.map {
                it.level
            }.toSet().sorted()
            val tasksMap = hashMapOf<Int, List<Deferred<Pair<String, Boolean>>>>()
            for (level in levels) {
                tasksMap[level] = tasks.filter { it.level == level }.map {
                    async {
                        val success = try {
                            it.init(application)
                            true
                        } catch (e: Exception) {
                            e.printStackTrace()
                            false
                        }
                        it.taskName to success
                    }
                }
            }
            for (level in levels) {
                tasksMap[level]?.forEach {
                    it.await().printlnInitialResult()
                }
            }
        }

    }

    private fun Pair<String, Boolean>.printlnInitialResult() {
        if (second) {
            logD(TAG, "${first}初始化成功")
        } else {
            logE(TAG, "${first}初始化失败")
        }
    }


    private fun isMainProcess(): Boolean {
        val currProcessName = getCurrentProcessName()
        logD(TAG, "current process name :$currProcessName")
        return application.packageName == currProcessName
    }

    private fun getCurrentProcessName(): String? = try {
        val file = File("/proc/${Process.myPid()}/cmdline")
        val bufferedReader = BufferedReader(FileReader(file))
        val processName = bufferedReader.readLine().trim { it <= ' ' }
        bufferedReader.close()
        processName
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    companion object {
        private const val TAG = "InitTaskRunner"
    }
}

/**
 * 异步初始化任务，子线程中初始化
 */
inline fun asyncInitTask(
    taskName: String,
    level: Int = 0,
    onlyMainProcess: Boolean = true,
    crossinline block: (Application) -> Unit
): InitTask = object : InitTask {
    override val sync: Boolean
        get() = false
    override val taskName: String
        get() = taskName
    override val level: Int
        get() = level
    override val onlyMainProcess: Boolean
        get() = onlyMainProcess

    override fun init(application: Application) {
        block(application)
    }
}

/**
 * 同步初始化任务，立即执行
 */
inline fun syncInitTask(
    taskName: String,
    level: Int = 0,
    onlyMainProcess: Boolean = true,
    crossinline block: (Application) -> Unit
): InitTask = object : InitTask {
    override val sync: Boolean
        get() = true
    override val taskName: String
        get() = taskName
    override val level: Int
        get() = level
    override val onlyMainProcess: Boolean
        get() = onlyMainProcess

    override fun init(application: Application) {
        block(application)
    }
}

interface InitTask {
    val sync: Boolean
    val taskName: String
    val level: Int
    val onlyMainProcess: Boolean
    fun init(application: Application)
}