package br.com.Core;

//import static br.com.Model.Agendamento_.intervalo;
import javax.faces.bean.ManagedBean;
import javax.servlet.annotation.WebListener;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class AgendamentoJobExecucao {

//    public static void main(String[] args) throws SchedulerException {
//
//        JobDetail job = JobBuilder.newJob(AgendamentoJob.class).withIdentity(
//                "AgendamentoJob", "envioJSon").build();
//        // Gatilho - ou seja, quando irá chamar, neste caso, a cada 5 segundos
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity("AgendamentoJob", "envioJSon")
//                .withSchedule(cronSchedule("* 0/2 16/17 * * ?"))
//                .build();
//
//        //                .forJob("AgendamentoJob", "envioJSon")
//        // Agenda e voa lá!
//        org.quartz.Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);
//    }
    public static void main(String[] args) {

        try {
            // Registrando a classe que execurÃ¡ meus mÃ©todos de negÃ³cio
            JobDetail job = JobBuilder.newJob(AgendamentoJob.class)
                    .withIdentity("AgendamentoJob", "envioJSon").build();

            // Criado um objeto de intervalo de repetiÃ§Ã£o
            // No nosso caso serÃ¡ de 2 segundos
            SimpleScheduleBuilder intervalo = SimpleScheduleBuilder
                    .simpleSchedule().withIntervalInSeconds(2).repeatForever();

            // Criado um disparador
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("disparaTrigger", "envioJSon")
                    .withSchedule(intervalo).build();

            // Finalmente Ã© criado um objeto de agendamento
            // que recebe o JOB e o disparador!
            org.quartz.Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
