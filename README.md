Описание тестового задания:
1. TaskHandlerService и AnotherTaskHandlerService это имитация 2 разных тасков, которые выполняются TaskSchedulerService.
2. в TaskSchedulerService Реализованы принятие таска, удаление, а также отправка всех ивентов из диаграммы. Из минусов на данный момент, можно придумать вариант, чтобы не выделять 3 потока на 1 TaskHandlerService.
3. Из необязательных вещей не реализованы приоритеты и классы приоритетов.
