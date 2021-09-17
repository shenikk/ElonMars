# ElonMars
Приложение ElonMars разработано специально для будущих путешественников на Марс, которые с особой тщательностью готовятся к своему первому полету.

![image](https://user-images.githubusercontent.com/70407879/133837902-eea9aefb-9ff1-42aa-bc1f-d66d5e65f71c.png)

## Features
С приложением ElonMars вы сможете:

- Выбрать дату полета и не бояться, что вы пропустите это событие - вы получите оповещение в указанное время! Таймер всегда будет держать вас в курсе оставшихся до полета дней.

- Узнать больше о планетах и полюбоваться изображениями космоса на вкладке Gallery. 
Изображения можно также добавить в 'любимые' или обновить список фотографий. При загрузке фотографий ячейки анимируются. 
Экран поддерживает формат gif.
Все фотографии сохраняются в кэше приложения.

- На вкладке 'Mission' сможете внести все запланированные дела в календарь приложения и отмечать их как 'выполненные' или удалять при долгом нажатии. Задачи сохраняются в базе данных SQLite.
Задачи можно также внести в системный календарь телефона.

- На вкладке Weather вы всегда будете в курсе последних прогнозов погоды. Температуру можно посмотреть в удобном формате - в градусах Фаренгейт или Цельсия.
Во время загрузки ячейки анимируются. Погодные данные кэшируются, т.к. информация обновляется не каждый день. Для обновления погоды добавлено действие pull to refresh.

- В настройках приложения можно сменить тему, ознакомиться с Nasa api и оставить фидбек разработчику в Telegram :)

Приложение поддерживает Minimum SDK 21.

## Home screen
 ![elonMars0](https://user-images.githubusercontent.com/70407879/133833219-02567b48-9a0b-4b9c-81a7-6b7cc8313318.png)  ![elonMars1](https://user-images.githubusercontent.com/70407879/133832850-52969ab4-decf-4a7c-8816-82fffd46db4b.png)  ![elonMars2](https://user-images.githubusercontent.com/70407879/133832914-0bae3f5f-cbb7-4b07-b30f-6f0662dad354.png)

## Gallery & Detail Photo screens
![elonMars3](https://user-images.githubusercontent.com/70407879/133833355-dd967cef-1bb3-4526-9a4d-fe29b824847f.png)  ![elonMars4](https://user-images.githubusercontent.com/70407879/133833391-628e8ec9-ef83-4e5e-88a2-41a5fb8b3b5c.png)

## Mars Mission screen
![elonMars6](https://user-images.githubusercontent.com/70407879/133833463-cc47b7da-7ccc-459c-9f61-5e107095de71.png)  ![elonMars5](https://user-images.githubusercontent.com/70407879/133833518-0e0b5428-66ae-45b1-aed5-4386e454514c.png)

## Weather & Settings screens
![elonMars7](https://user-images.githubusercontent.com/70407879/133833579-3fd82a67-7765-4835-af18-0a110c9e399c.png)  ![elonMars8](https://user-images.githubusercontent.com/70407879/133834692-a4ce306b-64e9-4119-ac2a-620b6c59c23f.png)


## Technology stack & third-party libraries
- RxJava2 
- Dagger2
- ViewModel 
- LiveData 
- Navigation Component + safeArgs
- View Binding
- Material design components
- Moshi
- Retrofit2
- SQLite
- Glide
- SwipeToRefresh
- Lottie animation
- Shimmer library from Facebook

## Архитектура
Приложение построено по архитектурному шаблону MVVM. Для каждого экрана реализована ViewModel.

Подробная навигация по приложению
https://miro.com/app/board/o9J_kjdqoOs=/

![image](https://user-images.githubusercontent.com/70407879/133835962-8be5ef76-b1a7-4561-9202-ad8e2e95e632.png)


## Краш аналитика
Для анализа ошибок подключена система анализа крашей Sentry.

## Api
Thanks to https://api.nasa.gov/
Danielius Ratkevičius on LottieFiles: https://lottiefiles.com/40399-mars
<div>Icons made by <a href="https://www.flaticon.com/authors/icongeek26" title="Icongeek26">Icongeek26</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
