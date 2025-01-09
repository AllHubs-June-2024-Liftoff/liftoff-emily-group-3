window.addEventListener('DOMContentLoaded', () => {
    const moviesSearch = document.getElementById('searchMovies');
    moviesSearch.addEventListener('input', () => {
        if(moviesSearch.value.length > 0){
            showMovies();
        }
        else{
            removeUnselected();
        }
    });
});

function showMovies(){
    let moviesList = document.getElementById('movies');
    let movies = [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20];
    movies.forEach(movie => {
        const movieOption = document.createElement("option");
        movieOption.value = movie;
        movieOption.textContent = "Movie "+movie;
        if(!movieInList(movieOption)){
            moviesList.appendChild(movieOption);
        }
    });
}

function movieInList(newOption){
    let moviesList = document.getElementById('movies');
    let moviesOptions = Array.from(moviesList.options);
    return moviesOptions.some(option => option.value == newOption.value);
}

function removeUnselected(){
    let moviesList = document.getElementById('movies');
    let moviesOptions = Array.from(moviesList.options);
    moviesOptions.forEach(option => {
        if(!option.selected){
            moviesList.removeChild(option);
        }
    });
}