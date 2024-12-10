window.addEventListener("load", function() {
    let table = new DataTable('#userTable');
    document.querySelectorAll('[name="userRow"]').forEach(row => {
        row.addEventListener("click",(event)=>{
            let selectedUser = event.target.closest('tr');
            console.log(selectedUser);
        });
    });
});