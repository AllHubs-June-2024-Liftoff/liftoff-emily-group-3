window.addEventListener('DOMContentLoaded', () => {
    const membersSearch = document.getElementById('searchMembers');
    const debouncedFetch = debounce(() => fetchMembers(membersSearch.value), 500);
    updateDisplayFromForm();
    membersSearch.addEventListener('input', () => {
        if(membersSearch.value.length >= 3){
            debouncedFetch();
        }
        else{
            removeAll();
            setDefault();
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
    removeAll();
    let membersList = document.getElementById('memberSearchList');
    memberJson.forEach(member => {
        const memberOption = document.createElement("li");
        memberOption.value = member.id;
        memberOption.setAttribute("user_name" ,member.name);
        memberOption.classList.add("list-group-item", "selectableMember");

        const profileImage = document.createElement("img");
        profileImage.classList.add("rounded-circle");
        profileImage.setAttribute("src", member.image);
        profileImage.setAttribute("alt", member.name + " profile");
        profileImage.style.width = "40px";
        profileImage.style.height = "40px";

        const userData = document.createElement("strong");
        userData.textContent = member.name;
        userData.classList.add("ms-3");

        memberOption.appendChild(profileImage);
        memberOption.appendChild(userData);
        membersList.appendChild(memberOption);

        memberOption.addEventListener('click', (e) => {
            let target = e.target.closest('li');
            addUserToForm(target.value, target.attributes.user_name.value);
        });
    });
}

function removeAll(){
    let membersList = document.getElementById('memberSearchList');
    let memberOptions = Array.from(membersList.querySelectorAll('li'));
    memberOptions.forEach(option => {
        membersList.removeChild(option);
    });
}

function setDefault(){
    let membersList = document.getElementById('memberSearchList');
    const memberOption = document.createElement("li");
    memberOption.value = 0;
    memberOption.name = "userListItem";
    memberOption.textContent = "No users - Search above";
    memberOption.classList.add("list-group-item");
    membersList.appendChild(memberOption);
}

function addUserToForm(id, name){
    let membersForm = document.getElementById('members');
    const memberOption = document.createElement("option");
    memberOption.value = id;
    memberOption.textContent = name;
    memberOption.selected = true;
    if(!memberInList(memberOption)){
        membersForm.appendChild(memberOption);
    }

    updateDisplayFromForm();
}

function memberInList(newOption){
    let membersList = document.getElementById('members');
    let memberOptions = Array.from(membersList.options);
    return memberOptions.some(option => option.value == newOption.value);
}

function removeUserFromForm(user){
    let formMembers = document.getElementById('members');
    const membersOptions = formMembers.options;
    for (let i = 0; i < membersOptions.length; i++) {
        if (membersOptions[i].value === user) {
            membersOptions.remove(i);
            break;
        }
    }
    updateDisplayFromForm();
}

function updateDisplayFromForm(){
    let selectedMembers = document.getElementById('selectedMembers');
    selectedMembers.innerHTML = "";
    let membersForm = document.getElementById('members');
    let formOptions = Array.from(membersForm.options);
    formOptions.forEach(option => {
        let optionDiv = buildUserDiv(option.value, option.textContent)
    });
}

function buildUserDiv(value, name){
    let selectedMembers = document.getElementById('selectedMembers');
    const selectedMember = document.createElement('div');
    selectedMember.classList.add('px-1', 'border', 'border-primary', 'rounded-pill', 'd-flex', 'justify-content-between');
    selectedMember.setAttribute('value', value);

    const userData = document.createElement('div');
    userData.classList.add('me-3');
    userData.textContent = name;

    const closeButton = document.createElement('div');
    closeButton.classList.add('cursor-pointer');
    closeButton.textContent = 'X';

    selectedMember.appendChild(userData);
    selectedMember.appendChild(closeButton);

    selectedMembers.appendChild(selectedMember);
    closeButton.addEventListener('click', (e) => {
        removeUserFromForm(e.target.parentElement.attributes.value.value);
    });
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