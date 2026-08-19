package com.ladislaurenan.flowable_spring_demo;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/processo")
public class ProcessoController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @PostMapping("/{key}/iniciar")
    public String iniciar(@PathVariable String key) {
        runtimeService.startProcessInstanceByKey(key);
        return "Processo '" + key + "' iniciado com sucesso!";
    }

    @PostMapping("/clima/iniciar")
    public String iniciarClima(@RequestBody Map<String, Object> coordenadas) {
        ProcessInstance instancia = runtimeService.startProcessInstanceByKey("processoClima", coordenadas);
        return "Processo iniciado. ID: " + instancia.getId();
    }

    @GetMapping("/clima/resultado/{processInstanceId}")
    public String verResultadoClima(@PathVariable String processInstanceId) {
        HistoricVariableInstance variavel = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName("respostaClima")
                .singleResult();

        return variavel != null ? (String) variavel.getValue() : "Resultado não encontrado";
    }

    @GetMapping("/tarefas")
    public List<String> listarTarefas() {
        List<Task> tarefas = taskService.createTaskQuery().list();
        return tarefas.stream()
                .map(t -> t.getId() + " - " + t.getName() + " (responsável: " + t.getAssignee() + ")")
                .collect(Collectors.toList());
    }

    @PostMapping("/tarefas/{id}/completar")
    public String completarTarefa(@PathVariable String id) {
        taskService.complete(id);
        return "Tarefa " + id + " concluída!";
    }
}