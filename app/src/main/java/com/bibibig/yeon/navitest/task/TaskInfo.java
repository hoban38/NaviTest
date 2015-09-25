/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bibibig.yeon.navitest.task;

import com.google.api.client.util.Objects;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

public class TaskInfo implements Comparable<TaskInfo>, Cloneable {

    static final String FIELDS = "id,title";
    //static final String FEED_FIELDS = "items(" + FIELDS + ")";

    public String taskid;
    public String tasktitle;
    public String tasknote;
    public String taskcompleted;

    TaskInfo(String id, String summary) {
        this.taskid = id;
        this.tasktitle = summary;
    }
    TaskInfo(String id, String summary,String note) {
        this.taskid = id;
        this.tasktitle = summary;
        this.tasknote = note;
       // this.note = note;
    }

    TaskInfo(Task task) {
        update(task);
    }

    TaskInfo(TaskList task) {
        update(task);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(TaskInfo.class).add("id", taskid).add("title", tasktitle).add("note",tasknote)
                .toString();
    }

    public int compareTo(TaskInfo other) {
        return tasktitle.compareTo(other.tasktitle);
    }

    @Override
    public TaskInfo clone() {
        try {
            return (TaskInfo) super.clone();
        } catch (CloneNotSupportedException exception) {
            // should not happen
            throw new RuntimeException(exception);
        }
    }

    void update(Task task) {
        taskid = task.getId();
        tasktitle = task.getTitle();
        tasknote = task.getNotes();

    }

    void update(TaskList task) {
        taskid = task.getId();
        tasktitle = task.getTitle();
    }
}
