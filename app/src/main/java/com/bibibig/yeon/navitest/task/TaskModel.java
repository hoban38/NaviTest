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

import android.util.Log;

import com.google.api.services.tasks.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thread-safe model for the Google calendars.
 * 
 * @author Yaniv Inbar
 */
public class TaskModel {

  private final Map<String, TaskInfo> Tasks_map = new HashMap<String, TaskInfo>();

  int size() {
    synchronized (Tasks_map) {
      return Tasks_map.size();
    }
  }

  void remove(String id) {
    synchronized (Tasks_map) {
        Tasks_map.remove(id);
    }
  }

    TaskInfo get(String id) {
    synchronized (Tasks_map) {
      return Tasks_map.get(id);
    }
  }

  void add(Task taskToAdd) {
    synchronized (Tasks_map) {
        TaskInfo found = get(taskToAdd.getId());
      if (found == null) {
          Tasks_map.put(taskToAdd.getId(),    new TaskInfo(taskToAdd));
      } else {
        found.update(taskToAdd);
      }
    }
  }
/*
  void add(TaskList taskToAdd) {
    synchronized (Tasks_map) {
        TaskInfo found = get(taskToAdd.getId());
      if (found == null) {
          Tasks_map.put(taskToAdd.getId(), new TaskInfo(taskToAdd));
      } else {
        found.update(taskToAdd);
      }
    }
  }
*/

  void reset(List<Task> tasks) {
    synchronized (Tasks_map) {
        Tasks_map.clear();
      for (Task task : tasks) {
        Log.e("TaskModel", "items: " + task.getTitle());
        add(task);
        //add(task.getTitle());
      }

    }
  }

  public TaskInfo[] toSortedArray() {
    synchronized (Tasks_map) {
      List<TaskInfo> result = new ArrayList<TaskInfo>();
      for (TaskInfo task : Tasks_map.values()) {
        result.add(task.clone());
      }
      Collections.sort(result);
      return result.toArray(new TaskInfo[0]);
    }
  }
}
