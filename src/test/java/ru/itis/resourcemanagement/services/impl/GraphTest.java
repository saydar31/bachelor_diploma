package ru.itis.resourcemanagement.services.impl;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphTest {
    private static class Task{
        private String name;
        private List<String> predecessors;

        public Task(String name, List<String> predecessors) {
            this.name = name;
            this.predecessors = predecessors;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "name='" + name + '\'' +
                    ", predecessors=" + predecessors +
                    '}';
        }

        public String getName() {
            return name;
        }

        public List<String> getPredecessors() {
            return predecessors;
        }
    }
    public static void main(String[] args) {
        Graph<Task, DefaultEdge> directedGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("E", List.of("B")));
        tasks.add(new Task("D", Arrays.asList("A", "B")));
        tasks.add(new Task("A", List.of()));
        tasks.add(new Task("B", List.of("A")));
        tasks.add(new Task("C", Arrays.asList("D", "B")));
        tasks.add(new Task("F", List.of("E")));
        tasks.add(new Task("Z", List.of()));
        tasks.add(new Task("X", List.of("Mc")));
        Map<String, Task> taskNameToTaskMap = tasks.stream()
                .collect(Collectors.toMap(Task::getName, task -> task));
        for (Task task : tasks) {
            directedGraph.addVertex(task);
            for (String predecessor : task.getPredecessors()) {
                Task predecessorTask = taskNameToTaskMap.get(predecessor);
                directedGraph.addVertex(predecessorTask);
                directedGraph.addEdge(predecessorTask, task);
            }
        }
        TopologicalOrderIterator<Task, DefaultEdge> moreDependencyFirstIterator = new TopologicalOrderIterator<>(
                directedGraph);

        moreDependencyFirstIterator.forEachRemaining(System.out::println);
    }
}
