window.addEventListener('DOMContentLoaded', () => {
    const membersSearch = document.getElementById('searchMembers');
    const debouncedFetch = debounce(() => fetchMembers(membersSearch.value), 500);
    membersSearch.addEventListener('input', () => {
        if(membersSearch.value.length >= 3){
            debouncedFetch();
        }
        else{
            removeUnselected();
        }
    });
});

function fetchMembers(search) {
    const csrfToken = document.querySelector('input[name="_csrf"]').value;
    return new Promise((resolve, reject) => {
        fetch('/search/profiles?search='+search, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'CSRF-Token': csrfToken
            }
        })
            .then(response => {
                if (!response.ok) {
                    reject(`Error: ${response.statusText}`);
                    return;
                }
                return response.json();
            })
            .then(data => {
                if (Array.isArray(data)) {
                    displayMembers(data);
                } else {
                    console.error('Data is not an array:', data);
                }
            })
            .catch(error => reject(`Request failed: ${error}`));
    });
}

function displayMembers(memberJson){
    let membersList = document.getElementById('members');
    memberJson.forEach(member => {
        const memberOption = document.createElement("option");
        memberOption.value = member.id;
        memberOption.textContent = member.name;
        if(!memberInList(memberOption)){
            membersList.appendChild(memberOption);
        }
    });
}

function removeUnselected(){
    let membersList = document.getElementById('members');
    let memberOptions = Array.from(membersList.options);
    memberOptions.forEach(option => {
        if(!option.selected){
            membersList.removeChild(option);
        }
    });
}

function memberInList(newOption){
    let membersList = document.getElementById('members');
    let memberOptions = Array.from(membersList.options);
    return memberOptions.some(option => option.value == newOption.value);
}

function debounce(func, delay) {
    let timeoutId;
    return function (...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            func(...args);
        }, delay);
    };
}